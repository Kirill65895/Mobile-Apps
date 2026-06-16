package com.example.fitnesstracker.feature.workouts.api.domain.repository

import com.example.fitnesstracker.feature.workouts.api.domain.model.Workout
import kotlinx.coroutines.flow.Flow

/**
 * Контракт репозитория ОБЪЯВЛЕН в domain (критерий: интерфейсы в domain,
 * реализация в data). Это и есть точка инверсии зависимостей: data будет
 * реализовывать этот интерфейс, а другие фичи (statistics) — потреблять его.
 */
interface WorkoutRepository {
    fun observeWorkouts(): Flow<List<Workout>>
    suspend fun addWorkout(workout: Workout)
    suspend fun deleteWorkout(workout: Workout)
}
