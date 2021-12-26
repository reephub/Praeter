package com.praeter.ui.signup.userform

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.praeter.BuildConfig
import com.praeter.R
import com.praeter.data.remote.dto.user.User
import com.praeter.databinding.ActivityUserFormBinding
import com.praeter.navigator.Navigator
import com.praeter.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class UserFormActivity : BaseActivity() {

    private var _viewBinding: ActivityUserFormBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    private val mViewModel: UserFormViewModel by viewModels()

    val navigator: Navigator = Navigator(this@UserFormActivity)

    private var szGender: String? = ""
    private var isPasswordVisible: Boolean = false
    private lateinit var user: User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _viewBinding = ActivityUserFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSpinner()
        if (BuildConfig.DEBUG) preloadData()
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.e("onDestroy()")
        _viewBinding = null
    }

    fun changePasswordVisibility(view: View) {
        Timber.d("onPasswordVisibilityButtonClicked()")

        // If flag is false - password hidden (default)
        if (!isPasswordVisible) {
            binding.contentUserForm.inputPassword.transformationMethod =
                HideReturnsTransformationMethod.getInstance()
            binding.contentUserForm.btnPasswordVisibility.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_visibility_off
                )
            )
            // Tint color programmatically
            // https://stackoverflow.com/questions/20121938/how-to-set-tint-for-an-image-view-programmatically-in-android
            binding.contentUserForm.btnPasswordVisibility.setColorFilter(
                ContextCompat.getColor(
                    this,
                    R.color.purple_200
                ), PorterDuff.Mode.SRC_IN
            )
        } else {
            binding.contentUserForm.inputPassword.transformationMethod =
                PasswordTransformationMethod.getInstance()
            binding.contentUserForm.btnPasswordVisibility.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_visibility
                )
            )
            binding.contentUserForm.btnPasswordVisibility.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                PorterDuff.Mode.SRC_IN
            )
        }
        isPasswordVisible = !isPasswordVisible
    }

    fun continueClick(view: View) {
        Timber.d("onContinueButtonClicked()")
        Timber.d("check if field are correctly filled")
        submitForm()
    }


    /////////////////////////////////////
    //
    // CLASS METHODS
    //
    /////////////////////////////////////
    private fun setupSpinner() {
        val adapter: ArrayAdapter<*> = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            arrayOf(resources.getStringArray(R.array.user_form_gender)) as ArrayList<String>
        ) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(
                position: Int, convertView: View,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                val tv = view as TextView
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY)
                } else {
                    tv.setTextColor(Color.WHITE)
                }
                return view
            }
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.contentUserForm.spGender.adapter = adapter
        binding.contentUserForm.spGender.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    Timber.d("Position : %s", position)
                    when (position) {
                        1 -> szGender = "male"
                        2 -> szGender = "female"
                        else -> {
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Timber.e("Nothing selected")
                }
            }
    }

    @SuppressLint("SetTextI18n")
    private fun preloadData() {
        binding.contentUserForm.inputLastName.setText("Doe")
        binding.contentUserForm.inputFirstName.setText("John")
        binding.contentUserForm.inputEmail.setText("john.doe@test.fr")
        binding.contentUserForm.inputPassword.setText("johndoe")
        binding.contentUserForm.inputConfirmPassword.setText("johndoe")
        binding.contentUserForm.inputPhoneNumber.setText("06123456789")
    }

    /**
     * Validating form
     */
    private fun submitForm() {
        if (!validateGender()) {
            return
        }
        if (!validateLastName()) {
            return
        }
        if (!validateFirstName()) {
            return
        }
        if (!validateEmail()) {
            return
        }
        if (!validatePassword()) {
            return
        }
        if (!validateConfirmPassword()) {
            return
        }
        if (!validatePhone()) {
            return
        }
        if (!validateDateOfBirth()) {
            return
        }
        user = User(
            szGender,
            binding.contentUserForm.inputFirstName.text.toString(),
            binding.contentUserForm.inputLastName.text.toString(),
            binding.contentUserForm.inputEmail.text.toString(),
            binding.contentUserForm.inputPassword.text.toString(),
            binding.contentUserForm.inputPhoneNumber.text.toString(),
            binding.contentUserForm.inputDateOfBirth.text.toString()
        )
        Toast.makeText(this, "Thank You!", Toast.LENGTH_SHORT).show()
        goToPlanActivity(user)
    }

    private fun validateGender(): Boolean {
        if (null == szGender || szGender!!.isEmpty()) {
            Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun validateLastName(): Boolean {
        if (binding.contentUserForm.inputLastName.text.toString().trim { it <= ' ' }.isEmpty()) {
            binding.contentUserForm.inputLayoutLastName.error =
                getString(R.string.err_msg_form_last_name)
            requestFocus(binding.contentUserForm.inputLastName)
            return false
        } else {
            binding.contentUserForm.inputLayoutLastName.isErrorEnabled = false
        }
        return true
    }

    private fun validateFirstName(): Boolean {
        if (binding.contentUserForm.inputFirstName.text.toString().trim { it <= ' ' }.isEmpty()) {
            binding.contentUserForm.inputLayoutFirstName.error =
                getString(R.string.err_msg_form_first_name)
            requestFocus(binding.contentUserForm.inputFirstName)
            return false
        } else {
            binding.contentUserForm.inputLayoutFirstName.isErrorEnabled = false
        }
        return true
    }

    private fun validateEmail(): Boolean {
        val email = binding.contentUserForm.inputEmail.text.toString().trim { it <= ' ' }
        if (email.isEmpty() || !isValidEmail(email)) {
            binding.contentUserForm.inputLayoutEmail.error =
                getString(R.string.err_msg_form_email)
            requestFocus(binding.contentUserForm.inputEmail)
            return false
        } else {
            binding.contentUserForm.inputLayoutEmail.isErrorEnabled = false
        }
        return true
    }

    private fun validatePassword(): Boolean {
        if (binding.contentUserForm.inputPassword.text.toString().trim { it <= ' ' }.isEmpty()) {
            binding.contentUserForm.inputLayoutPassword.error =
                getString(R.string.err_msg_form_password)
            requestFocus(binding.contentUserForm.inputPassword)
            return false
        } else {
            binding.contentUserForm.inputLayoutPassword.isErrorEnabled = false
        }
        return true
    }

    private fun validateConfirmPassword(): Boolean {
        if (binding.contentUserForm.inputConfirmPassword.text.toString().trim { it <= ' ' }
                .isEmpty()
            && binding.contentUserForm.inputConfirmPassword.text.toString() != binding.contentUserForm.inputPassword.text.toString()
        ) {
            binding.contentUserForm.inputLayoutConfirmPassword.error =
                getString(R.string.err_msg_form_confirm_password)
            requestFocus(binding.contentUserForm.inputConfirmPassword)
            return false
        } else {
            binding.contentUserForm.inputLayoutConfirmPassword.isErrorEnabled = false
        }
        return true
    }

    private fun validatePhone(): Boolean {
        if (binding.contentUserForm.inputPhoneNumber.text.toString().trim { it <= ' ' }
                .isEmpty()) {
            binding.contentUserForm.inputLayoutPhoneNumber.error =
                getString(R.string.err_msg_form_phone_number)
            requestFocus(binding.contentUserForm.inputPhoneNumber)
            return false
        } else {
            binding.contentUserForm.inputLayoutPhoneNumber.isErrorEnabled = false
        }
        return true
    }

    private fun validateDateOfBirth(): Boolean {
        /*if (inputDateOfBirth.getText().toString().trim().isEmpty()) {
            inputLayoutDateOfBirth.setError(context.getString(R.string.err_msg_form_date_of_birth));
            requestFocus(inputDateOfBirth);
            return false;
        } else {
            inputLayoutDateOfBirth.setErrorEnabled(false);
        }
*/
        return true
    }

    private fun requestFocus(view: View?) {
        if (view!!.requestFocus()) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    private fun goToPlanActivity(user: User) {
        navigator.callPlanActivity(user)
    }

    companion object {
        private fun isValidEmail(email: String): Boolean {
            return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }
}