package com.app.data.di

import com.app.data.base.network.interceptors.TokenManager
import com.app.data.base.network.interceptors.TokenManagerImpl
import com.app.data.features.response.repository.MainRepository
import com.app.data.features.response.repository.MainRepositoryImpl
import com.app.data.features.response.repository.UserDataRepository
import com.app.data.features.response.repository.UserDataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BindModule {

    @Singleton
    @Binds
    abstract fun bindAuthRepository(repositoryImpl: MainRepositoryImpl): MainRepository


    @Singleton
    @Binds
    abstract fun bindTokenManager(tokenManagerImpl: TokenManagerImpl): TokenManager


    @Singleton
    @Binds
    abstract fun bindUserDataRepository(userDataRepositoryImpl: UserDataRepositoryImpl): UserDataRepository

}