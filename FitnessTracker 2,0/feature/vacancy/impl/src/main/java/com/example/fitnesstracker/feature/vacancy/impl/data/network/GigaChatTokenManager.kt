package com.example.fitnesstracker.feature.vacancy.impl.data.network

import com.example.fitnesstracker.core.security.SecureStorage
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Управление токеном GigaChat: кеширование в EncryptedSharedPreferences (через SecureStorage —
 * у DataStore нет шифрования, см. примечание методички) и автоматическое обновление.
 * Mutex защищает от одновременного обновления при параллельных запросах.
 */
@Singleton
internal class GigaChatTokenManager @Inject constructor(
    private val oauthApi: GigaOAuthApi,
    private val secureStorage: SecureStorage,
    private val authKey: GigaChatAuthKey,
) {
    private val mutex = Mutex()

    /** Возвращает валидный токен: из кеша, если не истёк, иначе запрашивает новый. */
    suspend fun getValidToken(): String = mutex.withLock {
        val cached = secureStorage.getString(GigaChatConstants.KEY_ACCESS_TOKEN)
        val expiresAt = secureStorage.getLong(GigaChatConstants.KEY_EXPIRES_AT)
        // Обновляем заранее (за 60 c до истечения), чтобы не словить 401 на границе.
        if (!cached.isNullOrBlank() && System.currentTimeMillis() < expiresAt - 60_000) {
            return@withLock cached
        }
        val response = oauthApi.getToken(
            basicAuth = "Basic ${authKey.value}",
            requestId = UUID.randomUUID().toString(),
        )
        secureStorage.putString(GigaChatConstants.KEY_ACCESS_TOKEN, response.accessToken)
        secureStorage.putLong(GigaChatConstants.KEY_EXPIRES_AT, response.expiresAt)
        response.accessToken
    }
}

/** Обёртка над авторизационным ключом из BuildConfig (внедряется через Hilt). */
internal data class GigaChatAuthKey(val value: String)
