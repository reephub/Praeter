package com.praeter.ui.mainactivity;



import com.praeter.di.scopes.ActivityScope;
import com.praeter.navigator.Navigator;
import com.praeter.ui.base.BasePresenter;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;


@Module
public abstract class MainActivityModule {

    @ActivityScope
    @Provides
    static MainActivityContract.View provideView(MainActivity activity) {
        return new MainActivityView(activity);
    }

    @ActivityScope
    @Provides
    static Navigator provideNavigator(MainActivity activity) {
        return new Navigator(activity);
    }

    @ActivityScope
    @Binds
    abstract BasePresenter providePresenter(MainActivityPresenter presenter);
}
