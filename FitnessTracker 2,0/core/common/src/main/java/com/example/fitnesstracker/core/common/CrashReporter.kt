package com.example.fitnesstracker.core.common

/**
 * ФАСАД сбора ошибок (Лаб. №8). Скрывает за собой конкретные сервисы крашлитики
 * (Firebase Crashlytics, AppMetrica, Sentry…). Код приложения — репозитории и
 * ViewModel — зависят ТОЛЬКО от этого интерфейса. Смена/добавление сервиса = новая
 * реализация в одном месте, без правок бизнес-логики.
 */
interface CrashReporter {
    /** Диагностическое событие (breadcrumb). Сохраняется в контексте ближайшего отчёта. */
    fun log(message: String)
    /** Некритичная (non-fatal) ошибка: приложение восстановилось, но факт нужно зафиксировать. */
    fun recordException(throwable: Throwable, context: Map<String, String> = emptyMap())
    /** Технический идентификатор пользователя (НЕ email/телефон — только хеш/внутренний id). */
    fun setUserId(id: String)
    /** Произвольный ключ для фильтрации отчётов (экран, режим авторизации, feature flag…). */
    fun setCustomKey(key: String, value: String)
}

/** Ключи и breadcrumbs для отчётов (чтобы не плодить «магические строки»). */
object CrashKeys {
    const val SCREEN = "screen"
    const val AUTH_PROVIDER = "auth_provider"
    const val LAST_ACTION = "last_action"
}
