package com.praeter.ui.splashscreen;


import com.praeter.di.scopes.ActivityScope;
import com.praeter.navigator.Navigator;
import com.praeter.ui.base.BasePresenter;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class SplashScreenModule {

    @ActivityScope
    @Provides
    static SplashScreenContract.View provideView(SplashScreenActivity activity) {
        return new SplashScreenView(activity);
    }

    @ActivityScope
    @Provides
    static Navigator provideNavigator(SplashScreenActivity activity) {
        return new Navigator(activity);
    }

    @ActivityScope
    @Binds
    abstract BasePresenter providePresenter(SplashScreenPresenter presenter);
}
