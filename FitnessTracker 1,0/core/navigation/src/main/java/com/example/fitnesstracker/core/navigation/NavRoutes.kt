package com.example.fitnesstracker.core.navigation

/**
 * Единый реестр маршрутов. Лежит в :core:navigation, поэтому любая фича
 * может сослаться на маршрут другой фичи, НЕ завися от её :impl модуля.
 */
object NavRoutes {
    const val WORKOUTS = "workouts"
    const val ADD_WORKOUT = "workouts/add"
    const val STATISTICS = "statistics"
}
