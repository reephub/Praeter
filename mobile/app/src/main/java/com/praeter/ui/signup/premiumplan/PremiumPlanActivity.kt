package com.praeter.ui.signup.premiumplan

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.praeter.R
import com.praeter.data.remote.dto.user.User
import com.praeter.databinding.ActivityPremiumPlanBinding
import com.praeter.navigator.Navigator
import com.praeter.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import timber.log.Timber

@AndroidEntryPoint
class PremiumPlanActivity : BaseActivity() {

    private var _viewBinding: ActivityPremiumPlanBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    private lateinit var dialog: ProgressDialog
    private var user: User? = null

    private val mViewModel: PremiumPlanViewModel by viewModels()

    val navigator: Navigator = Navigator(this@PremiumPlanActivity)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = ActivityPremiumPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModelsObservers()

        val extras: Bundle = intent.extras ?: return
        user = extras.getParcelable(EXTRA_USER)
    }


    override fun onDestroy() {
        super.onDestroy()
        Timber.e("onDestroy()")
        _viewBinding = null
    }

    fun continueClick(view: View) {
        if (!validateCardOwnerName()) {
            return
        }
        if (!validateCardNumber()) {
            return
        }


        // TODO : Check if spinner are filled
        /*if (!validateCardValidityMonth()) {
            return;
        }

        if (!validateCardValidityYear()) {
            return;
        }*/if (!validateCardCCV()) {
            return
        }

        // TODO : correct method : checkCreditCardInfo()
        mViewModel.checkCardValidity()
    }

    private fun initViewModelsObservers() {
        mViewModel.getShouldShowHideLoading().observe(this, {
            if (!it) hideLoading() else showLoading()
        })

        mViewModel.getEnableDisableUI().observe(this, {
            if (!it) disableUI() else enableUI()
        })
        mViewModel.getCreditCardSuccessful().observe(this, {})
        mViewModel.getCreditCardFailed().observe(this, {})
    }

    private fun showLoading() {
        dialog = ProgressDialog(this)
        dialog.setCancelable(false)
        dialog.setTitle("Loading")
        dialog.setMessage("Verifying your bank details. Please wait...")
        dialog.setIndeterminate(true)
        dialog.show()
    }

    private fun hideLoading() {
        if (dialog.isShowing) dialog.dismiss()
    }

    private fun disableUI() {
        binding.contentPremiumPlan.inputLayoutCreditCardOwnerName.isEnabled = false
        binding.contentPremiumPlan.inputLayoutCreditCardNumber.isEnabled = false
        binding.contentPremiumPlan.inputLayoutCreditCardCcv.isEnabled = false
        binding.btnContinue.isEnabled = false
    }

    private fun enableUI() {

        // Avoid crash
        // Caused by: android.view.ViewRootImpl$CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
        runOnUiThread {
            binding.contentPremiumPlan.inputLayoutCreditCardOwnerName.isEnabled = true
            binding.contentPremiumPlan.inputLayoutCreditCardNumber.isEnabled = true
            binding.contentPremiumPlan.inputLayoutCreditCardCcv.isEnabled = true
            binding.btnContinue.isEnabled = true
        }
    }

    private fun onSuccessfulCreditCard() {
        Snackbar
            .make(
                findViewById(R.id.content),
                "onSuccessfulCreditCard()",
                BaseTransientBottomBar.LENGTH_LONG
            )
            .setBackgroundTint(ContextCompat.getColor(this, android.R.color.holo_green_dark))
            .show()

        CoroutineScope(Dispatchers.IO).launch {
            delay(2000)
            withContext(Dispatchers.Main) {
                user?.let { goToSuccessfulSignUpActivity(it) }
            }
        }
    }

    private fun onFailedCreditCard() {
        Snackbar
            .make(
                findViewById(R.id.content),
                "onFailedCreditCard()",
                BaseTransientBottomBar.LENGTH_LONG
            )
            .setBackgroundTint(ContextCompat.getColor(this, android.R.color.holo_red_dark))
            .show()
    }

    private fun goToSuccessfulSignUpActivity(user: User) {
        navigator.callSuccessfulSignUpActivity(user)
    }

    // Validating name
    private fun validateCardOwnerName(): Boolean {
        val cardOwnerName: String =
            binding.contentPremiumPlan.inputCreditCardOwnerName.text.toString()
                .trim { it <= ' ' }
        if (cardOwnerName.isEmpty()) {
            binding.contentPremiumPlan.inputLayoutCreditCardOwnerName.error =
                getString(R.string.err_msg_email)
            requestFocus(binding.contentPremiumPlan.inputCreditCardOwnerName)
            return false
        } else {
            binding.contentPremiumPlan.inputLayoutCreditCardOwnerName.isErrorEnabled = false
        }
        return true
    }

    // Validating number
    private fun validateCardNumber(): Boolean {
        if (binding.contentPremiumPlan.inputCreditCardNumber.text.toString().trim { it <= ' ' }
                .isEmpty()
            || binding.contentPremiumPlan.inputCreditCardNumber.length() != 16
        ) {
            binding.contentPremiumPlan.inputLayoutCreditCardNumber.error =
                getString(R.string.err_msg_credit_card_number)
            requestFocus(binding.contentPremiumPlan.inputCreditCardNumber)
            return false
        } else {
            binding.contentPremiumPlan.inputLayoutCreditCardNumber.isErrorEnabled = false
        }
        return true
    }

    // Validating ccv
    private fun validateCardCCV(): Boolean {
        if (binding.contentPremiumPlan.inputCreditCardCcv.text.toString().trim { it <= ' ' }
                .isEmpty()
            || binding.contentPremiumPlan.inputCreditCardCcv.length() != 3
        ) {
            binding.contentPremiumPlan.inputLayoutCreditCardCcv.error =
                getString(R.string.err_msg_credit_card_ccv)
            requestFocus(binding.contentPremiumPlan.inputCreditCardCcv)
            return false
        } else {
            binding.contentPremiumPlan.inputLayoutCreditCardCcv.isErrorEnabled = false
        }
        return true
    }

    private fun requestFocus(view: View?) {
        if (view!!.requestFocus()) {
            window
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    companion object {
        const val EXTRA_USER = "EXTRA_USER"
    }
}