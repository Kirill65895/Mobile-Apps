package com.example.fitnesstracker.feature.auth.impl.di

import com.example.fitnesstracker.core.navigation.FeatureEntry
import com.example.fitnesstracker.feature.auth.api.AuthService
import com.example.fitnesstracker.feature.auth.impl.data.AuthServiceImpl
import com.example.fitnesstracker.feature.auth.impl.ui.AuthFeatureEntry
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AuthModule {
    @Binds
    @Singleton
    abstract fun bindAuthService(impl: AuthServiceImpl): AuthService

    @Binds
    @IntoSet
    abstract fun bindFeatureEntry(entry: AuthFeatureEntry): FeatureEntry
}
