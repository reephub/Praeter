package com.praeter.di.component;

import android.app.Application;

import com.praeter.PraeterApplication;
import com.praeter.data.DataModule;
import com.praeter.di.module.ActivityModule;
import com.praeter.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        ApplicationModule.class,
        DataModule.class,
        ActivityModule.class
})
public interface ComponentInjector extends AndroidInjector<PraeterApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        Builder applicationModule(ApplicationModule applicationModule);

        Builder dataModule(DataModule dataModule);

        ComponentInjector build();
    }
}
