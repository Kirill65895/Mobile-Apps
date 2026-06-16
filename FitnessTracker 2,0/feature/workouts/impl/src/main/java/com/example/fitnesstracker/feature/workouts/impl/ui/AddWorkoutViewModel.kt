package com.example.fitnesstracker.feature.workouts.impl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesstracker.core.common.AnalyticsEvents
import com.example.fitnesstracker.core.common.AnalyticsService
import com.example.fitnesstracker.feature.workouts.api.domain.model.WorkoutType
import com.example.fitnesstracker.feature.workouts.api.domain.usecase.AddWorkoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddWorkoutViewModel @Inject constructor(
    private val addWorkout: AddWorkoutUseCase,
    private val analytics: AnalyticsService,
) : ViewModel() {

    fun save(type: WorkoutType, durationMinutes: Int, onDone: () -> Unit) {
        viewModelScope.launch {
            val result = runCatching { addWorkout(type, durationMinutes) }
            if (result.isSuccess) {
                analytics.trackEvent(
                    AnalyticsEvents.WORKOUT_ADDED,
                    mapOf(
                        AnalyticsEvents.PARAM_WORKOUT_TYPE to type.name,
                        AnalyticsEvents.PARAM_DURATION to durationMinutes,
                    ),
                )
            } else {
                analytics.trackError("add_workout_failed", result.exceptionOrNull())
            }
            onDone()
        }
    }
}
