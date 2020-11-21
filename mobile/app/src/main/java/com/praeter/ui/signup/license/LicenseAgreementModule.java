package com.praeter.ui.signup.license;

import com.praeter.di.scopes.ActivityScope;
import com.praeter.navigator.Navigator;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class LicenseAgreementModule {

    @ActivityScope
    @Provides
    static LicenseAgreementContract.View provideView(LicenseAgreementActivity activity) {
        return new LicenseAgreementView(activity);
    }

    @ActivityScope
    @Provides
    static Navigator provideNavigator(LicenseAgreementActivity activity) {
        return new Navigator(activity);
    }

}

