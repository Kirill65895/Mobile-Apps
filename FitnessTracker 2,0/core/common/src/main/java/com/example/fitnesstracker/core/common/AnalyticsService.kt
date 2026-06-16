package com.example.fitnesstracker.core.common

/**
 * ФАСАД аналитики (Лаб. №6). Код приложения зависит ТОЛЬКО от этого интерфейса
 * и ничего не знает о конкретном поставщике (AppMetrica, MyTracker и т.д.).
 * Интерфейс лежит в :core:common (чистый Kotlin), поэтому доступен всем модулям
 * без зависимости от Android-SDK. Смена провайдера = новая реализация в одном месте.
 */
interface AnalyticsService {
    fun trackEvent(name: String, params: Map<String, Any> = emptyMap())
    fun trackError(message: String, error: Throwable? = null)
}

/** Имена событий аналитики собраны в одном месте, чтобы не плодить «магические строки». */
object AnalyticsEvents {
    const val SCREEN_VIEWED = "screen_viewed"
    const val WORKOUT_ADDED = "workout_added"
    const val WORKOUT_DELETED = "workout_deleted"
    const val USER_LOGGED_IN = "user_logged_in"

    const val PARAM_SCREEN_NAME = "screen_name"
    const val PARAM_PROVIDER = "provider"
    const val PARAM_WORKOUT_TYPE = "workout_type"
    const val PARAM_DURATION = "duration_minutes"
}
