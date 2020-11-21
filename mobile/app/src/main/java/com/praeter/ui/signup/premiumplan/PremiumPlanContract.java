package com.praeter.ui.signup.premiumplan;

import com.praeter.ui.base.BaseView;

public class PremiumPlanContract {

    interface View extends BaseView {
        void showLoading();

        void hideLoading();

        void disableUI();

        void enableUI();

        void onSuccessfulCreditCard();

        void onFailedCreditCard();

        void closeApp();
    }

    interface Presenter {

        void checkCreditCardInfo(String cardOwnerName, String cardNumber, String cardValidityMonth, String cardValidityYear, String cardCCV);

        void checkCardValidity();

        void goToSuccessfulSignUpActivity();
    }
}
