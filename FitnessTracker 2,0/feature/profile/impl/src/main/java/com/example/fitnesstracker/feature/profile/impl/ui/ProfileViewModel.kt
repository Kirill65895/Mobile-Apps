package com.example.fitnesstracker.feature.profile.impl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesstracker.core.common.AnalyticsEvents
import com.example.fitnesstracker.core.common.AnalyticsService
import com.example.fitnesstracker.core.common.RemoteConfigKeys
import com.example.fitnesstracker.core.common.RemoteConfigService
import com.example.fitnesstracker.feature.auth.api.AuthService
import com.example.fitnesstracker.feature.profile.api.domain.model.UserProfile
import com.example.fitnesstracker.feature.profile.api.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
) : ViewModel() {

    private val user = authService.getCurrentUser()

    // Realtime-профиль из Firestore (или пустой поток, если не авторизованы).
    private val profileFlow = user?.let { profileRepository.observeProfile(it.id) } ?: flowOf(null)

    // Значения Remote Config + пересчёт при каждом обновлении конфигурации.
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
        // Сохраняем профиль текущего пользователя в Firestore при входе на экран.
        user?.let { u ->
            viewModelScope.launch {
                profileRepository.saveProfile(
                    UserProfile(
                        id = u.id, name = u.name, email = u.email,
                        fcmToken = null, updatedAtMillis = System.currentTimeMillis(),
                    )
                )
            }
        }
        viewModelScope.launch { remoteConfig.refresh() }
    }

    /** Выход из аккаунта: очищает сессию (токен, имя, провайдер). */
    fun logout() = authService.logout()

    fun onScreenViewed() {
        analytics.trackEvent(
            AnalyticsEvents.SCREEN_VIEWED,
            mapOf(AnalyticsEvents.PARAM_SCREEN_NAME to "profile"),
        )
    }
}
