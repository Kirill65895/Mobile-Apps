package com.example.fitnesstracker.feature.vacancy.impl.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.fitnesstracker.core.navigation.FeatureEntry
import com.example.fitnesstracker.core.navigation.NavRoutes
import javax.inject.Inject

internal class VacancyFeatureEntry @Inject constructor() : FeatureEntry {
    override fun NavGraphBuilder.register(navController: NavHostController) {
        composable(NavRoutes.VACANCY) { VacancyScreen() }
    }
}
