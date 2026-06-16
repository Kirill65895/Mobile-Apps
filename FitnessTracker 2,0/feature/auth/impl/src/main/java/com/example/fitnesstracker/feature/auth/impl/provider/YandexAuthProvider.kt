package com.example.fitnesstracker.feature.auth.impl.provider

import android.app.Activity
import com.example.fitnesstracker.feature.auth.api.AuthProvider
import com.example.fitnesstracker.feature.auth.api.User
import kotlinx.coroutines.delay
import java.util.UUID
import javax.inject.Inject

/**
 * Провайдер входа через Яндекс ID (LoginSDK). ДЕМО-ЗАГЛУШКА (см. VkAuthProvider).
 *
 * Реальная реализация (после подключения com.yandex.android:authsdk):
 * --------------------------------------------------------------------
 * val sdk = YandexAuthSdk.create(YandexAuthOptions(activity))
 * val intent = sdk.contract.createIntent(activity, YandexAuthLoginOptions())
 * // запуск через registerForActivityResult(sdk.contract) { result ->
 * //   when (result) {
 * //     is YandexAuthResult.Success -> cont.resume(Success(user, result.token.value, ...))
 * //     is YandexAuthResult.Failure -> cont.resume(Error(result.exception.message))
 * //     YandexAuthResult.Cancelled  -> cont.resume(Cancelled)
 * //   } }
 * --------------------------------------------------------------------
 */
internal class YandexAuthProvider @Inject constructor() : AuthProviderDelegate {
    override val provider = AuthProvider.YANDEX

    override suspend fun login(activity: Activity): ProviderResult {
        delay(600)
        return ProviderResult.Success(
            user = User(
                id = UUID.randomUUID().toString(),
                name = "Демо-пользователь Яндекс",
                email = "demo@yandex.ru",
                avatarUrl = null,
                provider = AuthProvider.YANDEX,
            ),
            accessToken = "ya-demo-" + UUID.randomUUID(),
            refreshToken = "ya-refresh-" + UUID.randomUUID(),
            expiresAtMillis = System.currentTimeMillis() + 24 * 60 * 60 * 1000L,
        )
    }
}
