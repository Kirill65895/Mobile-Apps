package com.example.fitnesstracker.feature.workouts.impl.di

import com.example.fitnesstracker.core.navigation.BottomNavItem
import com.example.fitnesstracker.core.navigation.BottomNavProvider
import com.example.fitnesstracker.core.navigation.FeatureEntry
import com.example.fitnesstracker.core.navigation.NavRoutes
import com.example.fitnesstracker.feature.workouts.api.domain.repository.WorkoutRepository
import com.example.fitnesstracker.feature.workouts.impl.data.WorkoutRepositoryImpl
import com.example.fitnesstracker.feature.workouts.impl.ui.WorkoutsFeatureEntry
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

/**
 * DI-граф фичи. Связывает интерфейс репозитория (api) с реализацией (impl)
 * и регистрирует FeatureEntry/BottomNavProvider в общие множества через @IntoSet.
 */
@Module
@InstallIn(SingletonComponent::class)
internal abstract class WorkoutsModule {

    @Binds
    @Singleton
    abstract fun bindWorkoutRepository(impl: WorkoutRepositoryImpl): WorkoutRepository

    @Binds
    @IntoSet
    abstract fun bindFeatureEntry(entry: WorkoutsFeatureEntry): FeatureEntry

    companion object {
        @Provides
        @IntoSet
        fun provideBottomNav(): BottomNavProvider = object : BottomNavProvider {
            override val item = BottomNavItem(NavRoutes.WORKOUTS, "Тренировки", order = 0)
        }
    }
}
