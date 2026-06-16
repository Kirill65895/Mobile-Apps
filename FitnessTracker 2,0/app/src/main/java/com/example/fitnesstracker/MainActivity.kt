package com.example.fitnesstracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.fitnesstracker.core.navigation.BottomNavProvider
import com.example.fitnesstracker.core.navigation.FeatureEntry
import com.example.fitnesstracker.core.navigation.NavRoutes
import com.example.fitnesstracker.core.ui.theme.FitnessTrackerTheme
import com.example.fitnesstracker.feature.auth.api.AuthService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var featureEntries: Set<@JvmSuppressWildcards FeatureEntry>
    @Inject lateinit var bottomNavProviders: Set<@JvmSuppressWildcards BottomNavProvider>
    @Inject lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Лаб. №6: если есть действующая сохранённая сессия — пропускаем вход.
        val startDestination =
            if (authService.isLoggedIn()) NavRoutes.WORKOUTS else NavRoutes.LOGIN

        setContent {
            FitnessTrackerTheme {
                AppRoot(
                    startDestination = startDestination,
                    featureEntries = featureEntries,
                    bottomNavItems = bottomNavProviders.map { it.item }.sortedBy { it.order },
                )
            }
        }
    }
}
