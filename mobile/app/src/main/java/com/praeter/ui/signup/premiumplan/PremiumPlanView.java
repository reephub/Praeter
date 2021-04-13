package com.praeter.ui.signup.premiumplan;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Spinner;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.praeter.R;
import com.praeter.data.remote.dto.User;
import com.praeter.ui.base.BaseViewImpl;

import org.parceler.Parcels;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class PremiumPlanView extends BaseViewImpl<PremiumPlanPresenter>
        implements PremiumPlanContract.View {

    public static final String EXTRA_USER = "EXTRA_USER";

    // TAG & Context
    private PremiumPlanActivity context;

    //Views
    //Credit Card Owner Name
    @BindView(R.id.input_layout_credit_card_owner_name)
    TextInputLayout inputLayoutCreditCardOwnerName;
    @BindView(R.id.input_credit_card_owner_name)
    TextInputEditText inputCreditCardOwnerName;
    // Credit Card Number
    @BindView(R.id.input_layout_credit_card_number)
    TextInputLayout inputLayoutCreditCardNumber;
    @BindView(R.id.input_credit_card_number)
    TextInputEditText inputCreditCardNumber;
    // Credit Card Validity Month
    @BindView(R.id.sp_card_month)
    Spinner spCardMonth;
    // Credit Card Validity Year
    @BindView(R.id.sp_card_year)
    Spinner spCardYear;
    // Credit Card CCV
    @BindView(R.id.input_layout_credit_card_ccv)
    TextInputLayout inputLayoutCreditCardCCV;
    @BindView(R.id.input_credit_card_ccv)
    TextInputEditText inputCreditCardCCV;

    @BindView(R.id.btn_continue)
    Button btnContinue;

    ProgressDialog dialog;

    private User user;


    @Inject
    PremiumPlanView(PremiumPlanActivity context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        getPresenter().attachView(this);

        ButterKnife.bind(this, context.findViewById(android.R.id.content));

        context.getSupportActionBar().setTitle(context.getString(R.string.title_activity_premium_plan));

        Bundle extras = context.getIntent().getExtras();

        if (null == extras)
            return;

        user = Parcels.unwrap(extras.getParcelable(EXTRA_USER));
    }


    @Override
    public void showLoading() {
        dialog = new ProgressDialog(context);

        dialog.setCancelable(false);
        dialog.setTitle("Loading");
        dialog.setMessage("Verifying your bank details. Please wait...");
        dialog.setIndeterminate(true);

        dialog.show();
    }

    @Override
    public void hideLoading() {
        if (null != dialog && dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    public void disableUI() {
        inputLayoutCreditCardOwnerName.setEnabled(false);
        inputLayoutCreditCardNumber.setEnabled(false);
        inputLayoutCreditCardCCV.setEnabled(false);
        btnContinue.setEnabled(false);
    }

    @Override
    public void enableUI() {

        // Avoid crash
        // Caused by: android.view.ViewRootImpl$CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
        context.runOnUiThread(() -> {
            inputLayoutCreditCardOwnerName.setEnabled(true);
            inputLayoutCreditCardNumber.setEnabled(true);
            inputLayoutCreditCardCCV.setEnabled(true);
            btnContinue.setEnabled(true);
        });

    }

    @Override
    public void onSuccessfulCreditCard() {
        Snackbar
                .make(
                        context.findViewById(android.R.id.content),
                        "onSuccessfulCreditCard()",
                        BaseTransientBottomBar.LENGTH_LONG)
                .setBackgroundTint(ContextCompat.getColor(context, android.R.color.holo_green_dark))
                .show();

        Completable.complete()
                .delay(2, TimeUnit.SECONDS)
                .doOnComplete(() -> getPresenter().goToSuccessfulSignUpActivity(user))
                .doOnError(Timber::e)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @Override
    public void onFailedCreditCard() {

        Snackbar
                .make(
                        context.findViewById(android.R.id.content),
                        "onFailedCreditCard()",
                        BaseTransientBottomBar.LENGTH_LONG)
                .setBackgroundTint(ContextCompat.getColor(context, android.R.color.holo_red_dark))
                .show();
    }


    @OnClick(R.id.btn_continue)
    void onContinueButtonClicked() {

        if (!validateCardOwnerName()) {
            return;
        }

        if (!validateCardNumber()) {
            return;
        }


        // TODO : Check if spinner are filled
        /*if (!validateCardValidityMonth()) {
            return;
        }

        if (!validateCardValidityYear()) {
            return;
        }*/


        if (!validateCardCCV()) {
            return;
        }

        // TODO : correct method : checkCreditCardInfo()
        getPresenter().checkCardValidity();
    }


    // Validating name
    private boolean validateCardOwnerName() {
        String cardOwnerName = inputCreditCardOwnerName.getText().toString().trim();

        if (cardOwnerName.isEmpty()) {
            inputLayoutCreditCardOwnerName.setError(context.getString(R.string.err_msg_email));
            requestFocus(inputCreditCardOwnerName);
            return false;
        } else {
            inputLayoutCreditCardOwnerName.setErrorEnabled(false);
        }

        return true;
    }


    // Validating number
    private boolean validateCardNumber() {
        if (inputCreditCardNumber.getText().toString().trim().isEmpty()
                || inputCreditCardNumber.length() != 16) {
            inputLayoutCreditCardNumber.setError(context.getString(R.string.err_msg_credit_card_number));
            requestFocus(inputCreditCardNumber);
            return false;
        } else {
            inputLayoutCreditCardNumber.setErrorEnabled(false);
        }

        return true;
    }


    // Validating ccv
    private boolean validateCardCCV() {
        if (inputCreditCardCCV.getText().toString().trim().isEmpty()
                || inputCreditCardCCV.length() != 3) {
            inputLayoutCreditCardCCV.setError(context.getString(R.string.err_msg_credit_card_ccv));
            requestFocus(inputCreditCardCCV);
            return false;
        } else {
            inputLayoutCreditCardCCV.setErrorEnabled(false);
        }

        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            context
                    .getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void closeApp() {
        context.finish();
    }

    @Override
    public void onDestroy() {
        getPresenter().detachView();
        context = null;
    }
}
