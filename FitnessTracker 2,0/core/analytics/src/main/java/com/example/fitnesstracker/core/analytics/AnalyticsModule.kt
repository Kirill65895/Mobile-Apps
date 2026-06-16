package com.example.fitnesstracker.core.analytics

import com.example.fitnesstracker.core.common.AnalyticsService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Связывает интерфейс AnalyticsService с реализацией на AppMetrica. */
@Module
@InstallIn(SingletonComponent::class)
internal abstract class AnalyticsModule {
    @Binds
    @Singleton
    abstract fun bindAnalyticsService(impl: AppMetricaAnalyticsService): AnalyticsService
}
