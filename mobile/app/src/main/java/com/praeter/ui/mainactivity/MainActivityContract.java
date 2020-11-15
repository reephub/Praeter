package com.praeter.ui.mainactivity;


import android.content.Context;

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
        /**
         * Ask the user for location permission
         *
         * @param context
         */
        void hasPermissions(Context context);
    }
}
