package com.praeter.ui.signup.license;

import com.praeter.navigator.Navigator;
import com.praeter.ui.base.BasePresenterImpl;
import com.praeter.ui.mainactivity.MainActivity;
import com.praeter.ui.mainactivity.MainActivityContract;
import com.praeter.ui.mainactivity.MainActivityView;

import javax.inject.Inject;

public class LicenseAgreementPresenter
        extends BasePresenterImpl<LicenseAgreementView> {


    @Inject
    LicenseAgreementActivity activity;

    @Inject
    Navigator navigator;

    @Inject
    LicenseAgreementPresenter() {
    }

}
