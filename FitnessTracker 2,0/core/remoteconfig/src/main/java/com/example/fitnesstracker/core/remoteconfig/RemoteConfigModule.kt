package com.example.fitnesstracker.core.remoteconfig

import com.example.fitnesstracker.core.common.RemoteConfigService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RemoteConfigModule {
    @Binds
    @Singleton
    abstract fun bindRemoteConfigService(impl: FirebaseRemoteConfigService): RemoteConfigService
}
