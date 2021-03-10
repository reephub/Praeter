package com.praeter.ui.signup.plan;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.annotation.Nullable;

import com.praeter.R;
import com.praeter.data.remote.dto.User;
import com.praeter.navigator.Navigator;
import com.praeter.ui.base.SimpleActivity;

import org.parceler.Parcels;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("NonConstantResourceId")
public class PlanActivity extends SimpleActivity {

    public static final String EXTRA_USER = "EXTRA_USER";

    @BindView(R.id.rb_free_plan)
    RadioButton rbFreePlan;

    @BindView(R.id.rb_premium_plan)
    RadioButton rbPremiumPlan;

    @BindView(R.id.btn_continue)
    Button btnContinue;

    @Inject
    Navigator navigator;

    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        ButterKnife.bind(this);

        getSupportActionBar().setTitle(R.string.title_activity_plan);

        navigator = new Navigator(PlanActivity.this);

        Bundle extras = getIntent().getExtras();

        if (null == extras) {
            return;
        }

        user = Parcels.unwrap(extras.getParcelable(EXTRA_USER));
    }


    @OnClick(R.id.btn_continue)
    void onContinueButtonClicked() {

        if (rbFreePlan.isChecked()) {
            user.setPremium(false);
            navigator.callSuccessfulSignUpActivity(user);
        }

        if (rbPremiumPlan.isChecked()) {
            user.setPremium(true);
            navigator.callPremiumPlanActivity(user);
        }

        finish();
    }

}
