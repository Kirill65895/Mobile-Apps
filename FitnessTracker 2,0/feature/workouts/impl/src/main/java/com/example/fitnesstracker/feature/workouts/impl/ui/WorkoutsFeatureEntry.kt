package com.example.fitnesstracker.feature.workouts.impl.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.fitnesstracker.core.navigation.FeatureEntry
import com.example.fitnesstracker.core.navigation.NavRoutes
import javax.inject.Inject

/**
 * Реализация контракта FeatureEntry. Регистрирует экраны фичи в общем графе.
 * :app не знает об этом классе напрямую — получает его через Hilt @IntoSet.
 */
internal class WorkoutsFeatureEntry @Inject constructor() : FeatureEntry {
    override fun NavGraphBuilder.register(navController: NavHostController) {
        composable(NavRoutes.WORKOUTS) {
            WorkoutsScreen(onAddClick = { navController.navigate(NavRoutes.ADD_WORKOUT) })
        }
        composable(NavRoutes.ADD_WORKOUT) {
            AddWorkoutScreen(onSaved = { navController.popBackStack() })
        }
    }
}
