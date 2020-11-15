package com.praeter.ui.signup.userform;

import com.praeter.ui.base.BasePresenterImpl;

import javax.inject.Inject;

public class UserFormPresenter extends BasePresenterImpl<UserFormView>
        implements UserFormContract.Presenter {


    @Inject
    UserFormActivity activity;

    @Inject
    UserFormPresenter() {
    }
}
