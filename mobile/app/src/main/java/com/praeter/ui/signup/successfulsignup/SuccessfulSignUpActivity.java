package com.praeter.ui.signup.successfulsignup;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.praeter.R;
import com.praeter.ui.base.BaseActivity;

public class SuccessfulSignUpActivity extends BaseActivity<SuccessfulSignUpView> {

    public static final String EXTRA_USER = "EXTRA_USER";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful_sign_up);
    }

    @Override
    protected void onDestroy() {
        view.onDestroy();
        super.onDestroy();
    }
}
