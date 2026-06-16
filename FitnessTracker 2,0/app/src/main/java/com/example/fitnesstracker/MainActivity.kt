package com.example.fitnesstracker

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.fitnesstracker.core.navigation.BottomNavProvider
import com.example.fitnesstracker.core.navigation.FeatureEntry
import com.example.fitnesstracker.core.navigation.NavRoutes
import com.example.fitnesstracker.core.ui.theme.FitnessTrackerTheme
import com.example.fitnesstracker.fcm.NotificationHelper
import com.example.fitnesstracker.feature.auth.api.AuthService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var featureEntries: Set<@JvmSuppressWildcards FeatureEntry>
    @Inject lateinit var bottomNavProviders: Set<@JvmSuppressWildcards BottomNavProvider>
    @Inject lateinit var authService: AuthService

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { /* результат не критичен */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationPermissionIfNeeded()

        val startDestination =
            if (authService.isLoggedIn()) NavRoutes.WORKOUTS else NavRoutes.LOGIN
        // Если приложение открыто нажатием на уведомление — маршрут перехода.
        val deepLinkRoute = intent?.getStringExtra(NotificationHelper.EXTRA_ROUTE)

        setContent {
            FitnessTrackerTheme {
                AppRoot(
                    startDestination = startDestination,
                    deepLinkRoute = deepLinkRoute,
                    featureEntries = featureEntries,
                    bottomNavItems = bottomNavProviders.map { it.item }.sortedBy { it.order },
                )
            }
        }
    }

    /** Лаб. №7: на Android 13+ показ уведомлений требует runtime-разрешения. */
    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
            if (!granted) notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}
