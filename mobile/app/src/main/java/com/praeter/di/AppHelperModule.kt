package com.praeter.di

import android.content.Context
import com.praeter.data.IRepository
import com.praeter.data.RepositoryImpl
import com.praeter.data.local.DbImpl
import com.praeter.data.remote.ApiImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object AppHelperModule {

    @Provides
    fun provideApiHelper(@ApplicationContext appContext: Context) =
        ApiImpl(
            ApiModule.provideDbApiService(appContext),
            ApiModule.provideUserApiService(appContext),
            ApiModule.provideGoogleApiService(appContext)
        )

    @Provides
    @ViewModelScoped // this is new
    fun provideRepository(dbImpl: DbImpl, apiImpl: ApiImpl) =
        RepositoryImpl(dbImpl, apiImpl) as IRepository
}