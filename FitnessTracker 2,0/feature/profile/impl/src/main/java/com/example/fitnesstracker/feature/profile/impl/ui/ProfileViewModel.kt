package com.example.fitnesstracker.feature.profile.impl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesstracker.core.common.AnalyticsEvents
import com.example.fitnesstracker.core.common.AnalyticsService
import com.example.fitnesstracker.core.common.CrashKeys
import com.example.fitnesstracker.core.common.CrashReporter
import com.example.fitnesstracker.core.common.EmptyProfileException
import com.example.fitnesstracker.core.common.RemoteConfigKeys
import com.example.fitnesstracker.core.common.RemoteConfigService
import com.example.fitnesstracker.feature.auth.api.AuthService
import com.example.fitnesstracker.feature.profile.api.domain.model.UserProfile
import com.example.fitnesstracker.feature.profile.api.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val profile: UserProfile? = null,
    val bannerText: String = "",
    val experimentEnabled: Boolean = false,
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val remoteConfig: RemoteConfigService,
    private val authService: AuthService,
    private val analytics: AnalyticsService,
    private val crashReporter: CrashReporter,   // Лаб. №8
) : ViewModel() {

    private val user = authService.getCurrentUser()

    private val profileFlow = user?.let { profileRepository.observeProfile(it.id) } ?: flowOf(null)

    private val remoteFlow = remoteConfig.updates().let { updates ->
        kotlinx.coroutines.flow.flow {
            emit(Unit)
            updates.collect { emit(Unit) }
        }
    }

    val state: StateFlow<ProfileUiState> =
        combine(profileFlow, remoteFlow) { profile, _ ->
            ProfileUiState(
                profile = profile,
                bannerText = remoteConfig.getString(RemoteConfigKeys.WELCOME_BANNER_TEXT),
                experimentEnabled = remoteConfig.getBoolean(RemoteConfigKeys.EXPERIMENTAL_FEATURE_ENABLED),
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ProfileUiState())

    init {
        crashReporter.setCustomKey(CrashKeys.SCREEN, "profile")
        user?.let { u ->
            viewModelScope.launch {
                // Отлов ошибок во ViewModel: при сбое сохранения фиксируем non-fatal с контекстом.
                runCatching {
                    profileRepository.saveProfile(
                        UserProfile(u.id, u.name, u.email, fcmToken = null,
                            updatedAtMillis = System.currentTimeMillis())
                    )
                }.onFailure {
                    crashReporter.recordException(it, mapOf(CrashKeys.SCREEN to "profile",
                        CrashKeys.LAST_ACTION to "save_profile"))
                }
            }
        }
        viewModelScope.launch { remoteConfig.refresh() }
    }

    fun onScreenViewed() {
        crashReporter.log("Открыт экран профиля")   // breadcrumb
        analytics.trackEvent(AnalyticsEvents.SCREEN_VIEWED,
            mapOf(AnalyticsEvents.PARAM_SCREEN_NAME to "profile"))
    }

    fun logout() = authService.logout()

    // --- Учебные триггеры для проверки крашлитики (Лаб. №8, задание 3) ---

    /** Фатальный краш: перед падением пишем breadcrumb, чтобы в отчёте было видно действие. */
    fun triggerTestCrash() {
        crashReporter.log("Пользователь нажал «Тестовый краш» на экране профиля")
        crashReporter.setCustomKey(CrashKeys.LAST_ACTION, "test_crash")
        throw RuntimeException("Тестовый краш из профиля (Лаб. №8)")
    }

    /** Non-fatal: приложение не падает, но ошибка фиксируется в отчёте. */
    fun triggerNonFatal() {
        crashReporter.log("Пользователь нажал «Тестовая non-fatal ошибка»")
        crashReporter.recordException(
            EmptyProfileException("Тестовая некритичная ошибка профиля"),
            mapOf(CrashKeys.SCREEN to "profile", CrashKeys.LAST_ACTION to "test_non_fatal"),
        )
    }
}
