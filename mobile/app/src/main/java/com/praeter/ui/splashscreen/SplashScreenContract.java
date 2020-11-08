package com.praeter.ui.splashscreen;

import android.content.Context;

import com.praeter.ui.base.BaseView;


public interface SplashScreenContract {

    interface View extends BaseView {
        void onPermissionsGranted();

        void onPermissionsDenied();

        void closeApp();
    }

    interface Presenter {
        void hasPermissions(Context context);

    }
}
