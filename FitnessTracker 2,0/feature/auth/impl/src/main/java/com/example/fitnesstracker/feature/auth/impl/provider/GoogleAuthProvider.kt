package com.example.fitnesstracker.feature.auth.impl.provider

import android.app.Activity
import com.example.fitnesstracker.feature.auth.api.AuthProvider
import com.example.fitnesstracker.feature.auth.api.User
import kotlinx.coroutines.delay
import java.util.UUID
import javax.inject.Inject

/**
 * Провайдер входа через Google по введённой почте. ДЕМО-ЗАГЛУШКА: формирует
 * пользователя из переданного e-mail (адрес уже провалидирован на UI).
 *
 * Реальная реализация (после подключения Google Identity / Credential Manager):
 * --------------------------------------------------------------------
 * val credentialManager = CredentialManager.create(activity)
 * val option = GetGoogleIdOption.Builder().setServerClientId(WEB_CLIENT_ID).build()
 * val request = GetCredentialRequest.Builder().addCredentialOption(option).build()
 * val result = credentialManager.getCredential(activity, request)
 * val token = GoogleIdTokenCredential.createFrom(result.credential.data)
 * // -> ProviderResult.Success(User(token.id, token.displayName, token.id, ...), token.idToken, ...)
 * --------------------------------------------------------------------
 */
internal class GoogleAuthProvider @Inject constructor() {
    val provider = AuthProvider.GOOGLE

    suspend fun login(activity: Activity, email: String): ProviderResult {
        delay(600) // имитация запроса к Google Identity
        val name = email.substringBefore('@').replaceFirstChar { it.uppercase() }
        return ProviderResult.Success(
            user = User(
                id = UUID.randomUUID().toString(),
                name = name,
                email = email,
                avatarUrl = null,
                provider = AuthProvider.GOOGLE,
            ),
            accessToken = "google-demo-" + UUID.randomUUID(),
            refreshToken = null,
            expiresAtMillis = System.currentTimeMillis() + 24 * 60 * 60 * 1000L,
        )
    }
}
