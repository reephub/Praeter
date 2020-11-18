package com.praeter.ui.signup.premiumplan;

import com.praeter.navigator.Navigator;
import com.praeter.ui.base.BasePresenterImpl;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

public class PremiumPlanPresenter extends BasePresenterImpl<PremiumPlanView>
        implements PremiumPlanContract.Presenter {


    @Inject
    PremiumPlanActivity activity;

    @Inject
    Navigator navigator;

    @Inject
    PremiumPlanPresenter() {
    }

    @Override
    public void checkCreditCardInfo(String cardOwnerName, String cardNumber,
                                    String cardValidityMonth, String cardValidityYear,
                                    String cardCCV) {

        getView().showLoading();

        if (cardOwnerName.trim().isEmpty()
                || cardNumber.trim().isEmpty()
                || cardValidityMonth.trim().isEmpty()
                || cardValidityYear.trim().isEmpty()
                || cardCCV.trim().isEmpty()) {
            Timber.e("One of field is ");

            return;

        }

    }

    @Override
    public void checkCardValidity() {

        getView().showLoading();

        getView().disableUI();

        Completable
                .complete()
                .delay(2, TimeUnit.SECONDS)
                .doOnComplete(() -> {

                    getView().hideLoading();

                    getView().onSuccessfulCreditCard();


                })
                .doOnError(throwable -> {
                    getView().hideLoading();
                    Timber.e(throwable);
                    // TODO : set on error to the view

                    getView().onFailedCreditCard();

                })
                .doAfterTerminate(() -> getView().enableUI())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }

    @Override
    public void goToSuccessfulSignUpActivity() {
        navigator.callSuccessfulSignUpActivity();
    }
}
