package com.praeter.ui.splashscreen;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.praeter.R;
import com.praeter.ui.base.BaseActivity;

public class SplashScreenActivity extends BaseActivity<SplashScreenView> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_splashscreen);
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onDestroy() {
        view.onDestroy();
        super.onDestroy();
    }
}
