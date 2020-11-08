package com.praeter.ui.mainactivity;

import android.os.Bundle;

import com.praeter.R;
import com.praeter.navigator.Navigator;
import com.praeter.ui.base.BaseActivity;

import javax.inject.Inject;

public class MainActivity extends BaseActivity<MainActivityView> {

    @Inject
    Navigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /**
         * Check for login session. If not logged in launch
         * login activity
         * */
        //TODO : Init check user exist in shared
        /*if (PraeterApplication.getInstance().getPrefManager().getUser() == null) {
            navigator.callLoginActivity();
        }*/

        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onDestroy() {
        view.onDestroy();
        super.onDestroy();
    }
}