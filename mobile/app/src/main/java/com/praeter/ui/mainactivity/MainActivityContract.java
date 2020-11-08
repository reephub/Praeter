package com.praeter.ui.mainactivity;


import com.praeter.ui.base.BaseView;

public interface MainActivityContract {

    interface View extends BaseView {

        /**
         * Shows loader progressBar
         */
        void showLoading();

        /**
         * Hide loader progressBar
         */
        void hideLoading();


        void closeApp();
    }

    interface Presenter {

    }
}
