package com.praeter.ui.login;


import com.praeter.di.scopes.ActivityScope;
import com.praeter.navigator.Navigator;
import com.praeter.ui.base.BasePresenter;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class LoginModule {


    @ActivityScope
    @Provides
    static LoginActivityContract.View provideView(LoginActivity activity) {
        return new LoginView(activity);
    }

    @ActivityScope
    @Provides
    static Navigator provideNavigator(LoginActivity activity) {
        return new Navigator(activity);
    }

    @ActivityScope
    @Binds
    abstract BasePresenter providePresenter(LoginPresenter presenter);
}
