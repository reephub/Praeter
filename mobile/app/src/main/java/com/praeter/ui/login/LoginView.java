package com.praeter.ui.login;

import com.praeter.ui.base.BaseViewImpl;

import javax.inject.Inject;

public class LoginView extends BaseViewImpl<LoginPresenter>
        implements LoginActivityContract.View {

    // TAG & Context
    private LoginActivity context;

    @Inject
    LoginView(LoginActivity context) {
        this.context = context;
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void closeApp() {

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }
}