package com.praeter.navigator;

import android.app.Activity;
import android.content.Intent;

import com.praeter.ui.login.LoginActivity;
import com.praeter.ui.mainactivity.MainActivity;
import com.praeter.ui.servicepicker.ServicePickerActivity;
import com.praeter.ui.splashscreen.SplashScreenActivity;

import javax.inject.Inject;

public class Navigator {

    private final Activity context;


    @Inject
    public Navigator(Activity context) {
        this.context = context;
    }

    public void callSplashActivity() {
        context.startActivity(new Intent(context, SplashScreenActivity.class));
    }

    public void callServicePickerActivity() {
        context.startActivity(new Intent(context, ServicePickerActivity.class));
    }


    public void callLoginActivity(String loginType) {
        Intent intent = new Intent(context, LoginActivity.class);

        intent.putExtra(LoginActivity.LOGIN_BUNDLE, LoginActivity.LOGIN_BUNDLE);
        intent.putExtra(LoginActivity.LOGIN_TYPE, loginType);

        context.startActivity(intent);
    }


    public void callSignUpActivity() {
        //context.startActivity(new Intent(context, SignUpActivity.class));
    }

    public void callMainActivity() {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    public void callIntentForPackageActivity(String intentPackageName) {
        context.startActivity(
                context
                        .getPackageManager()
                        .getLaunchIntentForPackage(intentPackageName));
    }
}
