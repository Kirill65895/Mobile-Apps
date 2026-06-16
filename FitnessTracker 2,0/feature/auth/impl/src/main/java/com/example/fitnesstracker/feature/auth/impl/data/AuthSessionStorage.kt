package com.example.fitnesstracker.feature.auth.impl.data

import com.example.fitnesstracker.core.security.SecureStorage
import com.example.fitnesstracker.feature.auth.api.AuthProvider
import com.example.fitnesstracker.feature.auth.api.User
import javax.inject.Inject

/**
 * Хранит сессию (токены + имя пользователя) в зашифрованном SecureStorage.
 * Инкапсулирует ключи — остальной код вызывает save()/currentUser()/isValid(),
 * не зная ни о шифровании, ни о наборе ключей (см. совет методички про TokenRepository).
 */
internal class AuthSessionStorage @Inject constructor(
    private val storage: SecureStorage,
) {
    fun save(user: User, accessToken: String, refreshToken: String?, expiresAtMillis: Long) {
        storage.putString(KEY_ACCESS, accessToken)
        refreshToken?.let { storage.putString(KEY_REFRESH, it) }
        storage.putLong(KEY_EXPIRES, expiresAtMillis)
        storage.putString(KEY_USER_ID, user.id)
        storage.putString(KEY_USER_NAME, user.name)
        storage.putString(KEY_PROVIDER, user.provider.key)
    }

    fun currentUser(): User? {
        val id = storage.getString(KEY_USER_ID) ?: return null
        val name = storage.getString(KEY_USER_NAME) ?: return null
        val providerKey = storage.getString(KEY_PROVIDER) ?: return null
        val provider = AuthProvider.entries.firstOrNull { it.key == providerKey } ?: return null
        return User(id = id, name = name, email = null, avatarUrl = null, provider = provider)
    }

    /** Токен есть и не истёк. */
    fun isValid(): Boolean {
        val token = storage.getString(KEY_ACCESS) ?: return false
        val expires = storage.getLong(KEY_EXPIRES, 0L)
        return token.isNotBlank() && expires > System.currentTimeMillis()
    }

    fun clear() = storage.clear()

    private companion object {
        const val KEY_ACCESS = "access_token"
        const val KEY_REFRESH = "refresh_token"
        const val KEY_EXPIRES = "expires_at"
        const val KEY_USER_ID = "user_id"
        const val KEY_USER_NAME = "user_name"
        const val KEY_PROVIDER = "provider"
    }
}
