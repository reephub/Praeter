package com.praeter.navigator;

import android.app.Activity;
import android.content.Intent;

import com.praeter.data.remote.dto.User;
import com.praeter.ui.login.LoginActivity;
import com.praeter.ui.mainactivity.MainActivity;
import com.praeter.ui.servicepicker.ServicePickerActivity;
import com.praeter.ui.signup.license.LicenseAgreementActivity;
import com.praeter.ui.signup.plan.PlanActivity;
import com.praeter.ui.signup.premiumplan.PremiumPlanActivity;
import com.praeter.ui.signup.successfulsignup.SuccessfulSignUpActivity;
import com.praeter.ui.signup.userform.UserFormActivity;
import com.praeter.ui.splashscreen.SplashScreenActivity;

import org.parceler.Parcels;

import javax.inject.Inject;

public class Navigator {

    private final Activity context;

    @Inject
    public Navigator(Activity context) {
        this.context = context;
    }

    // Splash
    public void callSplashActivity() {
        context.startActivity(new Intent(context, SplashScreenActivity.class));
    }

    // Service Picker
    public void callServicePickerActivity() {
        context.startActivity(new Intent(context, ServicePickerActivity.class));
    }


    // Login
    public void callLoginActivity(String loginType) {
        Intent intent = new Intent(context, LoginActivity.class);

        intent.putExtra(LoginActivity.LOGIN_BUNDLE, LoginActivity.LOGIN_BUNDLE);
        intent.putExtra(LoginActivity.LOGIN_TYPE, loginType);

        context.startActivity(intent);
    }


    // Sign Up
    public void callSignUpActivity() {
        context.startActivity(new Intent(context, LicenseAgreementActivity.class));
    }

    public void callUserFormActivity() {
        context.startActivity(new Intent(context, UserFormActivity.class));
    }

    public void callPlanActivity(User user) {
        Intent intent = new Intent(context, PlanActivity.class);
        intent.putExtra(PlanActivity.EXTRA_USER, Parcels.wrap(user));
        context.startActivity(intent);
    }

    public void callPremiumPlanActivity(User user) {
        Intent intent = new Intent(context, PremiumPlanActivity.class);
        intent.putExtra(PremiumPlanActivity.EXTRA_USER, Parcels.wrap(user));
        context.startActivity(intent);
    }

    public void callSuccessfulSignUpActivity(User user) {
        Intent intent = new Intent(context, SuccessfulSignUpActivity.class);
        intent.putExtra(SuccessfulSignUpActivity.EXTRA_USER, Parcels.wrap(user));
        context.startActivity(intent);
    }


    // Main Activity
    public void callMainActivity() {
        context.startActivity(new Intent(context, MainActivity.class));
    }


}
