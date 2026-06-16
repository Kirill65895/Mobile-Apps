package com.example.fitnesstracker.feature.auth.impl.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.fitnesstracker.core.navigation.FeatureEntry
import com.example.fitnesstracker.core.navigation.NavRoutes
import javax.inject.Inject

/** Регистрирует экран входа. Авторизация НЕ добавляет пункт в нижнее меню. */
internal class AuthFeatureEntry @Inject constructor() : FeatureEntry {
    override fun NavGraphBuilder.register(navController: NavHostController) {
        composable(NavRoutes.LOGIN) {
            LoginScreen(
                onLoggedIn = {
                    navController.navigate(NavRoutes.WORKOUTS) {
                        popUpTo(NavRoutes.LOGIN) { inclusive = true }
                    }
                },
            )
        }
    }
}
