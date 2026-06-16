package com.example.fitnesstracker.feature.about.di

import com.example.fitnesstracker.core.navigation.BottomNavItem
import com.example.fitnesstracker.core.navigation.BottomNavProvider
import com.example.fitnesstracker.core.navigation.FeatureEntry
import com.example.fitnesstracker.core.navigation.NavRoutes
import com.example.fitnesstracker.feature.about.ui.AboutFeatureEntry
import com.example.fitnesstracker.feature.about.ui.IntentMapNavigator
import com.example.fitnesstracker.feature.about.ui.MapNavigator
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AboutModule {

    @Binds
    abstract fun bindMapNavigator(impl: IntentMapNavigator): MapNavigator

    @Binds
    @IntoSet
    abstract fun bindFeatureEntry(entry: AboutFeatureEntry): FeatureEntry

    companion object {
        @Provides
        @IntoSet
        fun provideBottomNav(): BottomNavProvider = object : BottomNavProvider {
            override val item = BottomNavItem(NavRoutes.ABOUT, "О нас", order = 2)
        }
    }
}
