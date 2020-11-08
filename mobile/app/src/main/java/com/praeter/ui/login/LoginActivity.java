package com.praeter.ui.login;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.praeter.R;
import com.praeter.ui.base.BaseActivity;

public class LoginActivity extends BaseActivity<LoginView> {

    public static final String LOGIN_BUNDLE = "LOGIN_BUNDLE";

    public static final String LOGIN_TYPE = "LOGIN_TYPE";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        view.onDestroy();
        super.onDestroy();
    }
}
