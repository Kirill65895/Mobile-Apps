package com.example.fitnesstracker.feature.statistics.api.domain.usecase

import com.example.fitnesstracker.feature.statistics.api.domain.model.WorkoutStatistics
import com.example.fitnesstracker.feature.workouts.api.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Бизнес-логика статистики переиспользует ДАННЫЕ фичи workouts через её
 * публичный контракт (WorkoutRepository из :feature:workouts:api).
 * Никакого дублирования источника данных — это и есть combined-подход Google.
 */
class GetWorkoutStatisticsUseCase @Inject constructor(
    private val workoutRepository: WorkoutRepository,
) {
    operator fun invoke(): Flow<WorkoutStatistics> =
        workoutRepository.observeWorkouts().map { workouts ->
            val count = workouts.size
            val minutes = workouts.sumOf { it.durationMinutes }
            WorkoutStatistics(
                totalWorkouts = count,
                totalMinutes = minutes,
                totalCalories = workouts.sumOf { it.caloriesBurned },
                averageMinutes = if (count == 0) 0 else minutes / count,
            )
        }
}
