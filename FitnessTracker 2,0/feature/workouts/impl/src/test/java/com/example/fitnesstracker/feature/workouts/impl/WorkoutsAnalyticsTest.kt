package com.example.fitnesstracker.feature.workouts.impl

import com.example.fitnesstracker.core.common.AnalyticsEvents
import com.example.fitnesstracker.feature.workouts.api.domain.model.WorkoutType
import com.example.fitnesstracker.feature.workouts.api.domain.usecase.AddWorkoutUseCase
import com.example.fitnesstracker.feature.workouts.api.domain.usecase.DeleteWorkoutUseCase
import com.example.fitnesstracker.feature.workouts.api.domain.usecase.GetWorkoutsUseCase
import com.example.fitnesstracker.feature.workouts.impl.ui.AddWorkoutViewModel
import com.example.fitnesstracker.feature.workouts.impl.ui.WorkoutsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WorkoutsAnalyticsTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeWorkoutRepository()
    private val analytics = FakeAnalyticsService()
    private val crashReporter = FakeCrashReporter()

    @Test
    fun `adding a workout sends workout_added event`() = runTest {
        val viewModel = AddWorkoutViewModel(AddWorkoutUseCase(repository), analytics, crashReporter)

        viewModel.save(WorkoutType.RUNNING, durationMinutes = 30, onDone = {})
        advanceUntilIdle()

        assertTrue(AnalyticsEvents.WORKOUT_ADDED in analytics.names())
        val event = analytics.events.first { it.name == AnalyticsEvents.WORKOUT_ADDED }
        assertEquals("RUNNING", event.params[AnalyticsEvents.PARAM_WORKOUT_TYPE])
        assertEquals(30, event.params[AnalyticsEvents.PARAM_DURATION])
    }

    @Test
    fun `opening the workouts screen sends screen_viewed event`() = runTest {
        val viewModel = WorkoutsViewModel(
            getWorkouts = GetWorkoutsUseCase(repository),
            deleteWorkout = DeleteWorkoutUseCase(repository),
            analytics = analytics,
        )

        viewModel.onScreenViewed()

        val event = analytics.events.first { it.name == AnalyticsEvents.SCREEN_VIEWED }
        assertEquals("workouts", event.params[AnalyticsEvents.PARAM_SCREEN_NAME])
    }
}
