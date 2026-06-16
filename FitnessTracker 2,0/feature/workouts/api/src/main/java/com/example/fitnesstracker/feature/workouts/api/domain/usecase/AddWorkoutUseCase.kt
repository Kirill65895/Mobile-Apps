package com.example.fitnesstracker.feature.workouts.api.domain.usecase

import com.example.fitnesstracker.feature.workouts.api.domain.model.Workout
import com.example.fitnesstracker.feature.workouts.api.domain.model.WorkoutType
import com.example.fitnesstracker.feature.workouts.api.domain.repository.WorkoutRepository
import javax.inject.Inject

/**
 * Бизнес-правило: калории рассчитываются на основе типа и длительности,
 * длительность валидируется. Никакой зависимости от UI/Android/БД.
 */
class AddWorkoutUseCase @Inject constructor(
    private val repository: WorkoutRepository,
) {
    suspend operator fun invoke(type: WorkoutType, durationMinutes: Int) {
        require(durationMinutes in 1..600) { "Недопустимая длительность тренировки" }
        val workout = Workout(
            id = 0,
            type = type,
            durationMinutes = durationMinutes,
            caloriesBurned = type.caloriesPerMinute * durationMinutes,
            timestamp = System.currentTimeMillis(),
        )
        repository.addWorkout(workout)
    }
}
