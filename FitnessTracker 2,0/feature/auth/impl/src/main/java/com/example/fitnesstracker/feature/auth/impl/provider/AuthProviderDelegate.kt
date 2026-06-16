package com.example.fitnesstracker.feature.auth.impl.provider

import android.app.Activity
import com.example.fitnesstracker.feature.auth.api.AuthProvider
import com.example.fitnesstracker.feature.auth.api.User

/** Внутренний результат провайдера: содержит и пользователя, и токены. */
internal sealed interface ProviderResult {
    data class Success(
        val user: User,
        val accessToken: String,
        val refreshToken: String?,
        val expiresAtMillis: Long,
    ) : ProviderResult
    data class Error(val message: String) : ProviderResult
    data object Cancelled : ProviderResult
}

/** Контракт одного провайдера входа. За ним прячется конкретный SDK. */
internal interface AuthProviderDelegate {
    val provider: AuthProvider
    suspend fun login(activity: Activity): ProviderResult
}
