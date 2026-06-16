package com.example.fitnesstracker.feature.profile.impl.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.fitnesstracker.core.navigation.FeatureEntry
import com.example.fitnesstracker.core.navigation.NavRoutes
import javax.inject.Inject

internal class ProfileFeatureEntry @Inject constructor() : FeatureEntry {
    override fun NavGraphBuilder.register(navController: NavHostController) {
        composable(NavRoutes.PROFILE) {
            ProfileScreen(
                onLoggedOut = {
                    // После выхода — на экран входа, очищая весь стек навигации.
                    navController.navigate(NavRoutes.LOGIN) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                },
            )
        }
    }
}
