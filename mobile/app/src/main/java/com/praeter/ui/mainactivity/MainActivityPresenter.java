package com.praeter.ui.mainactivity;

import android.content.Context;

import com.praeter.navigator.Navigator;
import com.praeter.ui.base.BasePresenterImpl;

import javax.inject.Inject;

public class MainActivityPresenter extends BasePresenterImpl<MainActivityView>
        implements MainActivityContract.Presenter {

    @Inject
    MainActivity activity;

    @Inject
    Navigator navigator;

    @Inject
    MainActivityPresenter() {
    }

    @Override
    public void hasPermissions(Context context) {

    }
}
