package com.praeter.ui.signup.license;

import com.praeter.R;
import com.praeter.navigator.Navigator;
import com.praeter.ui.base.BasePresenterImpl;

import javax.inject.Inject;

public class LicenseAgreementPresenter extends BasePresenterImpl<LicenseAgreementView>
        implements LicenseAgreementContract.Presenter {

    @Inject
    LicenseAgreementActivity activity;

    @Inject
    Navigator navigator;

    @Inject
    LicenseAgreementPresenter() {
    }

    @Override
    public void setActionBarTitle() {

        activity
                .getSupportActionBar()
                .setTitle(activity.getString(R.string.title_activity_license_agreement));
    }

    @Override
    public void goToUserFormActivity() {
        if (activity != null && navigator != null) {
            navigator.callUserFormActivity();
            activity.finish();
        }
    }
}
