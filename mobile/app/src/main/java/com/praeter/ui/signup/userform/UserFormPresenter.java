package com.praeter.ui.signup.userform;

import com.praeter.data.remote.dto.User;
import com.praeter.navigator.Navigator;
import com.praeter.ui.base.BasePresenterImpl;

import javax.inject.Inject;

public class UserFormPresenter extends BasePresenterImpl<UserFormView>
        implements UserFormContract.Presenter {

    @Inject
    UserFormActivity activity;

    @Inject
    Navigator navigator;

    @Inject
    UserFormPresenter() {
    }

    @Override
    public void goToPlanActivity(User user) {
        if (null != activity && null != navigator) {
            navigator.callPlanActivity(user);
        }
    }
}
