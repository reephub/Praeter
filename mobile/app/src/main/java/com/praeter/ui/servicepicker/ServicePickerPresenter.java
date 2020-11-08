package com.praeter.ui.servicepicker;

import com.praeter.ui.base.BasePresenterImpl;

import javax.inject.Inject;

public class ServicePickerPresenter extends BasePresenterImpl<ServicePickerView>
        implements ServicePickerContract.Presenter {


    @Inject
    ServicePickerActivity activity;

    @Inject
    ServicePickerPresenter() {
    }

}
