package com.praeter.ui.signup.license;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.praeter.R;
import com.praeter.ui.base.BaseActivity;

import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;

public class LicenseAgreementActivity extends BaseActivity<LicenseAgreementView> {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_license_agreement);
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onDestroy() {
        view.onDestroy();
        super.onDestroy();
    }
}
