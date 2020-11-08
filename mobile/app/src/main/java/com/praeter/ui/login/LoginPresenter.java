package com.praeter.ui.login;

import com.praeter.ui.base.BasePresenterImpl;

import javax.inject.Inject;

public class LoginPresenter extends BasePresenterImpl<LoginView>
        implements LoginActivityContract.Presenter {


    @Inject
    LoginActivity activity;

    @Inject
    LoginPresenter() {
    }

}
