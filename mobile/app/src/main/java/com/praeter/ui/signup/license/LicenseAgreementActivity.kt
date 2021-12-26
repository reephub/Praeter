package com.praeter.ui.signup.license

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.praeter.R
import com.praeter.databinding.ActivityLicenseAgreementBinding
import com.praeter.navigator.Navigator
import com.praeter.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LicenseAgreementActivity : BaseActivity() {
    private var _viewBinding: ActivityLicenseAgreementBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    val navigator: Navigator = Navigator(this@LicenseAgreementActivity)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _viewBinding = ActivityLicenseAgreementBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun continueClick(view: View) {
        Timber.d("onContinueButtonClicked()")
        if (!binding.cbAcceptLicense.isChecked) {
            Timber.d("checkbox not checked display toast message")
            Toast.makeText(
                this,
                getString(R.string.err_msg_license_agreement_approval_mandatory),
                Toast.LENGTH_LONG
            )
                .show()
            return
        }
        Timber.d("user agreed go to the next activity")
        goToUserFormActivity()
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.e("onDestroy()")
        _viewBinding = null
    }

    private fun goToUserFormActivity() {
        navigator.callUserFormActivity()
        finish()
    }
}