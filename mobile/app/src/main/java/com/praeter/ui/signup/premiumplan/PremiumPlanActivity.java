package com.praeter.ui.signup.premiumplan;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.praeter.R;
import com.praeter.ui.base.BaseActivity;

public class PremiumPlanActivity extends BaseActivity<PremiumPlanView> {

    public static final String EXTRA_USER = "EXTRA_USER";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_premium_plan);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        view.onDestroy();
        super.onDestroy();
    }
}
