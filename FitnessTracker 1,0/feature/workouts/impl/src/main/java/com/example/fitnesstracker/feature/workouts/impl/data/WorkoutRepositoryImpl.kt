package com.example.fitnesstracker.feature.workouts.impl.data

import com.example.fitnesstracker.core.database.WorkoutDao
import com.example.fitnesstracker.feature.workouts.api.domain.model.Workout
import com.example.fitnesstracker.feature.workouts.api.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/** РЕАЛИЗАЦИЯ репозитория живёт в data-слое и зависит от источника данных. */
internal class WorkoutRepositoryImpl @Inject constructor(
    private val dao: WorkoutDao,
) : WorkoutRepository {

    override fun observeWorkouts(): Flow<List<Workout>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun addWorkout(workout: Workout) = dao.insert(workout.toEntity())

    override suspend fun deleteWorkout(workout: Workout) = dao.delete(workout.toEntity())
}
