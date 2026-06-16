package com.example.fitnesstracker.core.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

/**
 * Контракт фичи для встраивания в общий навигационный граф.
 *
 * Каждый feature:impl предоставляет реализацию этого интерфейса через Hilt
 * (@IntoSet). Модуль :app собирает Set<FeatureEntry> и строит NavHost,
 * НЕ зная о конкретных фичах напрямую. Так реализуется навигация между
 * feature-модулями без прямых зависимостей между ними (критерий №7.1).
 */
interface FeatureEntry {
    fun NavGraphBuilder.register(navController: NavHostController)
}

/** Описывает один пункт нижней навигации, предоставляемый фичей. */
data class BottomNavItem(
    val route: String,
    val label: String,
    val order: Int,
)

/** Поставщик пунктов нижнего меню. Реализуется фичами и собирается в :app. */
interface BottomNavProvider {
    val item: BottomNavItem
}
