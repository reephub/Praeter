package com.praeter.ui.login


import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.praeter.R
import com.praeter.data.remote.dto.user.User
import com.praeter.databinding.ActivityLoginBinding
import com.praeter.navigator.Navigator
import com.praeter.ui.base.BaseActivity
import timber.log.Timber

class LoginActivity : BaseActivity() {

    private var _viewBinding: ActivityLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    private val mViewModel: LoginViewModel by viewModels()

    private var isPasswordVisible: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListeners()
        initViewModelsObservers()
        preloadData()
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }

    fun changePasswordVisibility(view: View) {
        Timber.d("onPasswordVisibilityButtonClicked()")

        // If flag is false - password hidden (default)
        if (!isPasswordVisible) {
            binding.inputPassword.transformationMethod =
                HideReturnsTransformationMethod.getInstance()
            binding.btnPasswordVisibility.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_visibility_off
                )
            )
            // Tint color programmatically
            // https://stackoverflow.com/questions/20121938/how-to-set-tint-for-an-image-view-programmatically-in-android
            binding.btnPasswordVisibility.setColorFilter(
                ContextCompat.getColor(
                    this,
                    R.color.purple_200
                ), PorterDuff.Mode.SRC_IN
            )
        } else {
            binding.inputPassword.transformationMethod =
                PasswordTransformationMethod.getInstance()
            binding.btnPasswordVisibility.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_visibility
                )
            )
            binding.btnPasswordVisibility.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                PorterDuff.Mode.SRC_IN
            )
        }
        isPasswordVisible = !isPasswordVisible
    }


    /**
     * logging in user. Will make http post request with name, email
     * as parameters
     */
    fun login(view: View) {
        Timber.d("login()")

        if (!validateEmail()) {
            return
        }

        if (!validatePassword()) {
            return
        }

        hideKeyboard(this, findViewById(R.id.content))

        val email = binding.inputEmail.text.toString()
        val password = binding.inputPassword.text.toString()

        mViewModel.makeCallLogin(User(email, password))
    }

    fun registerNewAccount(view: View) {
        Navigator(this).callSignUpActivity()
    }

    private fun setListeners() {

        binding.inputEmail.addTextChangedListener(MyTextWatcher(binding.inputEmail))
        binding.inputPassword.addTextChangedListener(MyTextWatcher(binding.inputPassword))

        //https://stackoverflow.com/questions/17989733/move-to-another-edittext-when-soft-keyboard-next-is-clicked-on-android
        binding.inputPassword.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard(
                    this@LoginActivity,
                    this@LoginActivity.findViewById(android.R.id.content)
                )
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun initViewModelsObservers() {
        mViewModel.getShouldDisplayHideProgressBar().observe(this, {})
    }

    private fun preloadData() {
        binding.inputEmail.setText("jane.mills@test.fr")
        binding.inputPassword.setText("jm_test")
    }


    private fun requestFocus(view: View?) {
        if (view!!.requestFocus()) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    // Validating email
    private fun validateEmail(): Boolean {
        val email = binding.inputEmail.text.toString().trim { it <= ' ' }
        if (email.isEmpty() || !isValidEmail(email)) {
            binding.inputLayoutEmail.error = getString(com.praeter.R.string.err_msg_email)
            requestFocus(binding.inputEmail)
            return false
        } else {
            binding.inputLayoutEmail.isErrorEnabled = false
        }
        return true
    }

    // Validating password
    private fun validatePassword(): Boolean {
        if (binding.inputPassword.text.toString().trim { it <= ' ' }.isEmpty()) {
            binding.inputLayoutPassword.error = getString(com.praeter.R.string.err_msg_name)
            requestFocus(binding.inputPassword)
            return false
        } else {
            binding.inputLayoutPassword.isErrorEnabled = false
        }
        return true
    }


    private inner class MyTextWatcher(private val view: View?) : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun afterTextChanged(editable: Editable) {
            when (view!!.id) {
                R.id.input_password -> validatePassword()
                R.id.input_email -> validateEmail()
            }
        }
    }

    companion object {
        const val LOGIN_BUNDLE = "LOGIN_BUNDLE"
        const val LOGIN_TYPE = "LOGIN_TYPE"

        private fun isValidEmail(email: String): Boolean {
            return (!TextUtils.isEmpty(email)
                    && Patterns.EMAIL_ADDRESS.matcher(email).matches())
        }

        /**
         * Hide the keyboard
         *
         * @param view
         */
        private fun hideKeyboard(context: Context?, view: View) {
            val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}