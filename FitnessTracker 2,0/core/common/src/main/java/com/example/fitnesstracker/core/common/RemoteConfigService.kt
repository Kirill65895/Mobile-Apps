package com.example.fitnesstracker.core.common

import kotlinx.coroutines.flow.Flow

/**
 * ФАСАД удалённой конфигурации (Лаб. №7). Код приложения зависит только от этого
 * интерфейса и не знает о Firebase Remote Config. Смена источника конфигурации
 * (например, на свой бэкенд) = новая реализация в одном месте.
 */
interface RemoteConfigService {
    /** Запускает синхронизацию: первичная загрузка + подписка на обновления в реальном времени. */
    fun start()
    /** Ручная загрузка с сервера (fetchAndActivate). */
    suspend fun refresh()
    fun getString(key: String): String
    fun getBoolean(key: String): Boolean
    /** Эмитит при изменении конфигурации на сервере (Real-time Remote Config). */
    fun updates(): Flow<Unit>
}

/** Ключи параметров Remote Config (соответствуют параметрам в консоли Firebase). */
object RemoteConfigKeys {
    const val WELCOME_BANNER_TEXT = "welcome_banner_text"        // string
    const val EXPERIMENTAL_FEATURE_ENABLED = "experimental_feature_enabled" // boolean
}
