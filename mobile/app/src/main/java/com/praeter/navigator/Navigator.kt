package com.praeter.navigator

import android.app.Activity
import android.content.Intent
import com.praeter.data.remote.dto.user.User
import com.praeter.ui.login.LoginActivity
import com.praeter.ui.mainactivity.MainActivity
import com.praeter.ui.servicepicker.ServicePickerActivity
import com.praeter.ui.signup.license.LicenseAgreementActivity
import com.praeter.ui.signup.plan.PlanActivity
import com.praeter.ui.signup.premiumplan.PremiumPlanActivity
import com.praeter.ui.signup.successfulsignup.SuccessfulSignUpActivity
import com.praeter.ui.signup.userform.UserFormActivity
import com.praeter.ui.splashscreen.SplashScreenActivity
import javax.inject.Inject

class Navigator @Inject constructor(private val context: Activity) {

    // Splash
    fun callSplashActivity() {
        context.startActivity(Intent(context, SplashScreenActivity::class.java))
    }

    // Service Picker
    fun callServicePickerActivity() {
        context.startActivity(Intent(context, ServicePickerActivity::class.java))
    }

    // Login
    fun callLoginActivity(loginType: String?) {
        val intent = Intent(context, LoginActivity::class.java)
        intent.putExtra(LoginActivity.LOGIN_BUNDLE, LoginActivity.LOGIN_BUNDLE)
        intent.putExtra(LoginActivity.LOGIN_TYPE, loginType)
        context.startActivity(intent)
    }

    // Sign Up
    fun callSignUpActivity() {
        context.startActivity(Intent(context, LicenseAgreementActivity::class.java))
    }

    fun callUserFormActivity() {
        context.startActivity(Intent(context, UserFormActivity::class.java))
    }

    fun callPlanActivity(user: User?) {
        val intent = Intent(context, PlanActivity::class.java)
        intent.putExtra(PlanActivity.EXTRA_USER, user)
        context.startActivity(intent)
    }

    fun callPremiumPlanActivity(user: User?) {
        val intent = Intent(context, PremiumPlanActivity::class.java)
        intent.putExtra(PremiumPlanActivity.EXTRA_USER, user)
        context.startActivity(intent)
    }

    fun callSuccessfulSignUpActivity(user: User?) {
        val intent = Intent(context, SuccessfulSignUpActivity::class.java)
        intent.putExtra(SuccessfulSignUpActivity.EXTRA_USER, user)
        context.startActivity(intent)
    }

    // Main Activity
    fun callMainActivity() {
        context.startActivity(Intent(context, MainActivity::class.java))
    }
}