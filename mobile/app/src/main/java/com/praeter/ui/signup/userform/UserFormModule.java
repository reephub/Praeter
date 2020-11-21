package com.praeter.ui.signup.userform;

import com.praeter.di.scopes.ActivityScope;
import com.praeter.navigator.Navigator;
import com.praeter.ui.base.BasePresenter;
import com.praeter.ui.mainactivity.MainActivity;
import com.praeter.ui.mainactivity.MainActivityContract;
import com.praeter.ui.mainactivity.MainActivityPresenter;
import com.praeter.ui.mainactivity.MainActivityView;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class UserFormModule {


    @ActivityScope
    @Provides
    static UserFormContract.View provideView(UserFormActivity activity) {
        return new UserFormView(activity);
    }

    @ActivityScope
    @Provides
    static Navigator provideNavigator(UserFormActivity activity) {
        return new Navigator(activity);
    }


    @ActivityScope
    @Binds
    abstract BasePresenter providePresenter(UserFormPresenter presenter);
}
