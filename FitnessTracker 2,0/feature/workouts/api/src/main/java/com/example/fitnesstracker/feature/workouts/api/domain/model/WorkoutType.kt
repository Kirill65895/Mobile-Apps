package com.example.fitnesstracker.feature.workouts.api.domain.model

enum class WorkoutType(val displayName: String, val caloriesPerMinute: Int) {
    RUNNING("Бег", 11),
    CYCLING("Велосипед", 8),
    SWIMMING("Плавание", 9),
    STRENGTH("Силовая", 6),
    YOGA("Йога", 4),
    WALKING("Ходьба", 4),
}
