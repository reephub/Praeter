package com.praeter.ui.signup.successfulsignup;

import com.praeter.di.scopes.ActivityScope;
import com.praeter.navigator.Navigator;
import com.praeter.ui.base.BasePresenter;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class SuccessfulSignUpModule {

    @ActivityScope
    @Provides
    static SuccessfulSignUpContract.View provideView(SuccessfulSignUpActivity activity) {
        return new SuccessfulSignUpView(activity);
    }

    @ActivityScope
    @Provides
    static Navigator provideNavigator(SuccessfulSignUpActivity activity) {
        return new Navigator(activity);
    }

    @ActivityScope
    @Binds
    abstract BasePresenter providePresenter(SuccessfulSignUpPresenter presenter);
}
