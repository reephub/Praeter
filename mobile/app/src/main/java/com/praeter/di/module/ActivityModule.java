package com.praeter.di.module;


import com.praeter.di.scopes.ActivityScope;
import com.praeter.ui.mainactivity.MainActivity;
import com.praeter.ui.mainactivity.MainActivityModule;
import com.praeter.ui.splashscreen.SplashScreenActivity;
import com.praeter.ui.splashscreen.SplashScreenModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = SplashScreenModule.class)
    abstract SplashScreenActivity splashScreenActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity mainActivity();
}
