package com.example.fitnesstracker.feature.auth.impl.provider

import android.app.Activity
import com.example.fitnesstracker.feature.auth.api.AuthProvider
import com.example.fitnesstracker.feature.auth.api.User
import kotlinx.coroutines.delay
import java.util.UUID
import javax.inject.Inject

/**
 * Провайдер входа через ВКонтакте.
 *
 * ВНИМАНИЕ: ниже — РАБОЧАЯ ДЕМО-ЗАГЛУШКА, чтобы проект собирался и запускался без
 * регистрации приложения и ключей. Архитектурно это полноценный провайдер за
 * фасадом — реальный VK SDK подключается заменой ТОЛЬКО этого класса (см. README_LR6).
 *
 * Реальная реализация (после подключения com.vk:android-sdk-*):
 * --------------------------------------------------------------------
 * return suspendCancellableCoroutine { cont ->
 *     VK.login(activity, listOf(VKScope.EMAIL))
 *     // результат приходит в VKAuthCallback, зарегистрированный через
 *     // activity.registerForActivityResult(VK.getVKAuthActivityResultContract()) { ... }
 *     // onLogin(token) -> cont.resume(ProviderResult.Success(user, token.accessToken, ...))
 *     // onLoginFailed(e) -> cont.resume(if (e.isCanceled) Cancelled else Error(e.message))
 * }
 * --------------------------------------------------------------------
 */
internal class VkAuthProvider @Inject constructor() : AuthProviderDelegate {
    override val provider = AuthProvider.VK

    override suspend fun login(activity: Activity): ProviderResult {
        delay(600) // имитация сетевого запроса SDK
        return ProviderResult.Success(
            user = User(
                id = UUID.randomUUID().toString(),
                name = "Демо-пользователь VK",
                email = "demo@vk.com",
                avatarUrl = null,
                provider = AuthProvider.VK,
            ),
            accessToken = "vk-demo-" + UUID.randomUUID(),
            refreshToken = null,
            expiresAtMillis = System.currentTimeMillis() + 24 * 60 * 60 * 1000L,
        )
    }
}
