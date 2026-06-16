package com.example.fitnesstracker.core.security

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Реализация на EncryptedSharedPreferences: и ключи, и значения шифруются
 * AES-256 (значения — GCM, ключи — SIV), мастер-ключ лежит в аппаратном
 * Android Keystore и недоступен другим приложениям. Здесь хранятся токены
 * авторизации (Лаб. №6, требование: НЕ хранить токены в обычном SharedPreferences).
 */
@Singleton
internal class EncryptedSecureStorage @Inject constructor(
    @ApplicationContext private val context: Context,
) : SecureStorage {

    private val prefs: SharedPreferences by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        EncryptedSharedPreferences.create(
            context,
            "fitness_secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    override fun putString(key: String, value: String) { prefs.edit().putString(key, value).apply() }
    override fun getString(key: String): String? = prefs.getString(key, null)
    override fun putLong(key: String, value: Long) { prefs.edit().putLong(key, value).apply() }
    override fun getLong(key: String, default: Long): Long = prefs.getLong(key, default)
    override fun remove(key: String) { prefs.edit().remove(key).apply() }
    override fun clear() { prefs.edit().clear().apply() }
}
