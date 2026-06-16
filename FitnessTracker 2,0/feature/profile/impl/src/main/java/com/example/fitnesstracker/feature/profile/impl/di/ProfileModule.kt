package com.example.fitnesstracker.feature.profile.impl.di

import com.example.fitnesstracker.core.navigation.BottomNavItem
import com.example.fitnesstracker.core.navigation.BottomNavProvider
import com.example.fitnesstracker.core.navigation.FeatureEntry
import com.example.fitnesstracker.core.navigation.NavRoutes
import com.example.fitnesstracker.feature.profile.api.domain.repository.ProfileRepository
import com.example.fitnesstracker.feature.profile.impl.data.ProfileRepositoryImpl
import com.example.fitnesstracker.feature.profile.impl.ui.ProfileFeatureEntry
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ProfileModule {
    @Binds
    @Singleton
    abstract fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository

    @Binds
    @IntoSet
    abstract fun bindFeatureEntry(entry: ProfileFeatureEntry): FeatureEntry

    companion object {
        @Provides
        @IntoSet
        fun provideBottomNav(): BottomNavProvider = object : BottomNavProvider {
            override val item = BottomNavItem(NavRoutes.PROFILE, "Профиль", order = 3)
        }
    }
}
