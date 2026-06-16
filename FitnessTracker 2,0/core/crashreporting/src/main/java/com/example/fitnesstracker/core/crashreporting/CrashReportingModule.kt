package com.example.fitnesstracker.core.crashreporting

import com.example.fitnesstracker.core.common.CrashReporter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CrashReportingModule {
    @Binds
    @Singleton
    abstract fun bindCrashReporter(impl: CompositeCrashReporter): CrashReporter
}
