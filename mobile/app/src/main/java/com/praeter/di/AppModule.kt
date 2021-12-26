package com.praeter.di

import android.app.Activity
import com.praeter.navigator.Navigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {

    @Provides
    fun provideNavigator(activity: Activity): Navigator {
        return Navigator(activity)
    }
}