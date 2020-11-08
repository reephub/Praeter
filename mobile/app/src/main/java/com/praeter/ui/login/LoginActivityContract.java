package com.praeter.ui.login;

import com.praeter.ui.base.BaseView;

public interface LoginActivityContract {


    interface View extends BaseView {

        /**
         * Shows loader progressBar
         */
        void showLoading();

        /**
         * Hide loader progressBar
         */
        void hideLoading();

        void onLoginSuccessful();

        void onLoginFailed();

        void closeApp();
    }

    interface Presenter {

        void makeCallLogin();

    }
}
