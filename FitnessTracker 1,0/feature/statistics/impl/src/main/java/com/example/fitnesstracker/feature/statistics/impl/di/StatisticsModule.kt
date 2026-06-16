package com.example.fitnesstracker.feature.statistics.impl.di

import com.example.fitnesstracker.core.navigation.BottomNavItem
import com.example.fitnesstracker.core.navigation.BottomNavProvider
import com.example.fitnesstracker.core.navigation.FeatureEntry
import com.example.fitnesstracker.core.navigation.NavRoutes
import com.example.fitnesstracker.feature.statistics.impl.ui.StatisticsFeatureEntry
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
internal abstract class StatisticsModule {

    @Binds
    @IntoSet
    abstract fun bindFeatureEntry(entry: StatisticsFeatureEntry): FeatureEntry

    companion object {
        @Provides
        @IntoSet
        fun provideBottomNav(): BottomNavProvider = object : BottomNavProvider {
            override val item = BottomNavItem(NavRoutes.STATISTICS, "Статистика", order = 1)
        }
    }
}
