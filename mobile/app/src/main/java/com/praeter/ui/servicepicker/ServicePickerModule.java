package com.praeter.ui.servicepicker;

import com.praeter.di.scopes.ActivityScope;
import com.praeter.navigator.Navigator;
import com.praeter.ui.base.BasePresenter;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class ServicePickerModule {


    @ActivityScope
    @Provides
    static ServicePickerContract.View provideView(ServicePickerActivity activity) {
        return new ServicePickerView(activity);
    }

    @ActivityScope
    @Provides
    static Navigator provideNavigator(ServicePickerActivity activity) {
        return new Navigator(activity);
    }

    @ActivityScope
    @Binds
    abstract BasePresenter providePresenter(ServicePickerPresenter presenter);
}
