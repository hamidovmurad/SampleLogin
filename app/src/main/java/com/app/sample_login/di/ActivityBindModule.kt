package com.app.sample_login.di

import com.app.common.navigation.Navigator
import com.app.sample_login.navigation.RetainedNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ActivityBindModule {

    @Binds
    @ActivityRetainedScoped
    abstract fun bindNavigator(navigatorImpl: RetainedNavigator): Navigator
}