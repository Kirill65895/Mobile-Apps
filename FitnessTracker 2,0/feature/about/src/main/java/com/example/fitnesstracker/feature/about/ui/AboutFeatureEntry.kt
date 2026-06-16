package com.example.fitnesstracker.feature.about.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.fitnesstracker.core.navigation.FeatureEntry
import com.example.fitnesstracker.core.navigation.NavRoutes
import javax.inject.Inject

internal class AboutFeatureEntry @Inject constructor() : FeatureEntry {
    override fun NavGraphBuilder.register(navController: NavHostController) {
        composable(NavRoutes.ABOUT) { AboutScreen() }
    }
}
