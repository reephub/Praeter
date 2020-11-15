package com.praeter.ui.signup.license;

import android.annotation.SuppressLint;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.praeter.R;
import com.praeter.navigator.Navigator;
import com.praeter.ui.base.BaseViewImpl;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class LicenseAgreementView extends BaseViewImpl<LicenseAgreementPresenter>
        implements LicenseAgreementContract.View {


    @BindView(R.id.btn_continue)
    Button continueButton;

    @BindView(R.id.cb_accept_license)
    MaterialCheckBox cbAcceptLicense;


    @Inject
    Navigator navigator;


    private LicenseAgreementActivity context;


    @Inject
    LicenseAgreementView(LicenseAgreementActivity context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

        Timber.d("onCreate()");

        ButterKnife.bind(this, context.findViewById(android.R.id.content));

        context.getSupportActionBar().setTitle(context.getString(R.string.title_activity_license_agreement));

    }

    @OnClick(R.id.btn_continue)
    void onContinueButtonClicked() {
        Timber.d("onContinueButtonClicked()");

        if (!cbAcceptLicense.isChecked()) {
            Timber.d("checkbox not checked display toast message");
            Toast.makeText(
                    context,
                    context.getString(R.string.err_msg_license_agreement_approval_mandatory),
                    Toast.LENGTH_LONG)
                    .show();

            return;
        }

        Timber.d("user agreed go to the next activity");
        navigator.callUserFormActivity();

    }

    @Override
    public void onDestroy() {

        context = null;
    }
}
