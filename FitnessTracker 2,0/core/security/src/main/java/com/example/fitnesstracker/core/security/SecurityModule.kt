package com.example.fitnesstracker.core.security

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class SecurityModule {
    @Binds
    @Singleton
    abstract fun bindSecureStorage(impl: EncryptedSecureStorage): SecureStorage
}
