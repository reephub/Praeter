package com.praeter.ui.servicepicker;

import android.annotation.SuppressLint;
import android.widget.Button;

import com.praeter.R;
import com.praeter.navigator.Navigator;
import com.praeter.ui.base.BaseViewImpl;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("NonConstantResourceId")
public class ServicePickerView extends BaseViewImpl<ServicePickerPresenter>
        implements ServicePickerContract.View {


    @BindView(R.id.btn_search_service)
    Button btnSearchService;

    @BindView(R.id.btn_propose_service)
    Button btnProposeService;


    ServicePickerActivity context;


    @Inject
    Navigator navigator;

    @Inject
    ServicePickerView(ServicePickerActivity context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

        getPresenter().attachView(this);

        ButterKnife.bind(this, context.findViewById(android.R.id.content));
    }


    @OnClick(R.id.btn_search_service)
    void onSearchServiceButtonClicked() {
        if (navigator != null) {
            navigator.callLoginActivity("login_normal");
        }
    }

    @OnClick(R.id.btn_propose_service)
    void onProposeServiceButtonClicked() {
        if (navigator != null) {
            navigator.callLoginActivity("login_provider");
        }
    }

    @Override
    public void onDestroy() {
        getPresenter().detachView();
        context = null;
    }
}
