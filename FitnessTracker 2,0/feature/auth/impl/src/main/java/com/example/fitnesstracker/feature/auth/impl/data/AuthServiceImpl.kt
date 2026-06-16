package com.example.fitnesstracker.feature.auth.impl.data

import android.app.Activity
import com.example.fitnesstracker.core.common.AnalyticsEvents
import com.example.fitnesstracker.core.common.AnalyticsService
import com.example.fitnesstracker.feature.auth.api.AuthResult
import com.example.fitnesstracker.feature.auth.api.AuthService
import com.example.fitnesstracker.feature.auth.api.User
import com.example.fitnesstracker.feature.auth.impl.provider.AuthProviderDelegate
import com.example.fitnesstracker.feature.auth.impl.provider.GoogleAuthProvider
import com.example.fitnesstracker.feature.auth.impl.provider.ProviderResult
import com.example.fitnesstracker.feature.auth.impl.provider.VkAuthProvider
import com.example.fitnesstracker.feature.auth.impl.provider.YandexAuthProvider
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Реализация фасада авторизации. Делегирует вход нужному провайдеру, сохраняет
 * сессию в зашифрованное хранилище и логирует событие user_logged_in. Это
 * единственное место, связывающее провайдеры, хранилище и аналитику воедино.
 */
@Singleton
internal class AuthServiceImpl @Inject constructor(
    private val vk: VkAuthProvider,
    private val yandex: YandexAuthProvider,
    private val google: GoogleAuthProvider,
    private val session: AuthSessionStorage,
    private val analytics: AnalyticsService,
) : AuthService {

    override suspend fun loginWithVk(activity: Activity): AuthResult = login(vk, activity)
    override suspend fun loginWithYandex(activity: Activity): AuthResult = login(yandex, activity)

    override suspend fun loginWithGoogle(activity: Activity, email: String): AuthResult =
        handle(google.login(activity, email))

    private suspend fun login(delegate: AuthProviderDelegate, activity: Activity): AuthResult =
        handle(delegate.login(activity))

    /** Общая обработка результата провайдера: сохранить сессию и залогировать вход. */
    private fun handle(result: ProviderResult): AuthResult = when (result) {
        is ProviderResult.Success -> {
            session.save(result.user, result.accessToken, result.refreshToken, result.expiresAtMillis)
            analytics.trackEvent(
                AnalyticsEvents.USER_LOGGED_IN,
                mapOf(AnalyticsEvents.PARAM_PROVIDER to result.user.provider.key),
            )
            AuthResult.Success(result.user)
        }
        is ProviderResult.Error -> AuthResult.Error(result.message)
        ProviderResult.Cancelled -> AuthResult.Cancelled
    }

    override fun logout() = session.clear()
    override fun getCurrentUser(): User? = if (session.isValid()) session.currentUser() else null
    override fun isLoggedIn(): Boolean = session.isValid()
}
