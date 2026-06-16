package com.example.fitnesstracker.feature.workouts.impl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesstracker.core.common.AnalyticsEvents
import com.example.fitnesstracker.core.common.AnalyticsService
import com.example.fitnesstracker.core.common.CrashKeys
import com.example.fitnesstracker.core.common.CrashReporter
import com.example.fitnesstracker.feature.workouts.api.domain.model.WorkoutType
import com.example.fitnesstracker.feature.workouts.api.domain.usecase.AddWorkoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddWorkoutViewModel @Inject constructor(
    private val addWorkout: AddWorkoutUseCase,
    private val analytics: AnalyticsService,
    private val crashReporter: CrashReporter,   // Лаб. №8: отлов ошибок с контекстом
) : ViewModel() {

    fun save(type: WorkoutType, durationMinutes: Int, onDone: () -> Unit) {
        // Breadcrumb: видно действие пользователя перед возможной ошибкой.
        crashReporter.log("AddWorkout: save type=${type.name}, duration=$durationMinutes")
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
                val error = result.exceptionOrNull()
                analytics.trackError("add_workout_failed", error)
                // Non-fatal: приложение восстановилось, но факт фиксируем с контекстом.
                if (error != null) {
                    crashReporter.recordException(
                        error,
                        mapOf(
                            CrashKeys.SCREEN to "add_workout",
                            CrashKeys.LAST_ACTION to "save",
                            "workout_type" to type.name,
                            "duration" to durationMinutes.toString(),
                        ),
                    )
                }
            }
            onDone()
        }
    }
}
