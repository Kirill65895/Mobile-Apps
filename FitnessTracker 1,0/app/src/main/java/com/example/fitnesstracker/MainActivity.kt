package com.example.fitnesstracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.fitnesstracker.core.navigation.BottomNavProvider
import com.example.fitnesstracker.core.navigation.FeatureEntry
import com.example.fitnesstracker.core.ui.theme.FitnessTrackerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /** Hilt внедряет множества, собранные из ВСЕХ feature-модулей через @IntoSet. */
    @Inject lateinit var featureEntries: Set<@JvmSuppressWildcards FeatureEntry>
    @Inject lateinit var bottomNavProviders: Set<@JvmSuppressWildcards BottomNavProvider>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitnessTrackerTheme {
                AppRoot(
                    featureEntries = featureEntries,
                    bottomNavItems = bottomNavProviders.map { it.item }.sortedBy { it.order },
                )
            }
        }
    }
}
