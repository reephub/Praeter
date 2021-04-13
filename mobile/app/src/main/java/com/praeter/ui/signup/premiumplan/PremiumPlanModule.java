package com.praeter.ui.signup.premiumplan;

import com.praeter.di.scopes.ActivityScope;
import com.praeter.navigator.Navigator;
import com.praeter.ui.base.BasePresenter;
import com.praeter.ui.signup.successfulsignup.SuccessfulSignUpPresenter;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class PremiumPlanModule {

    @ActivityScope
    @Provides
    static PremiumPlanContract.View provideView(PremiumPlanActivity activity) {
        return new PremiumPlanView(activity);
    }

    @ActivityScope
    @Provides
    static Navigator provideNavigator(PremiumPlanActivity activity) {
        return new Navigator(activity);
    }

    @ActivityScope
    @Binds
    abstract BasePresenter providePresenter(PremiumPlanPresenter presenter);
}
