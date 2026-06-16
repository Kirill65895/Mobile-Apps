package com.example.fitnesstracker.core.crashreporting

import com.example.fitnesstracker.core.common.CrashReporter
import io.appmetrica.analytics.AppMetrica
import javax.inject.Inject

/**
 * Реализация фасада поверх AppMetrica. Краши (необработанные исключения) AppMetrica
 * собирает автоматически после активации; здесь реализованы non-fatal через
 * reportError и привязка пользователя. Всё защищено runCatching.
 */
internal class AppMetricaCrashReporter @Inject constructor() : CrashReporter {

    override fun log(message: String) {
        // У AppMetrica нет прямого аналога breadcrumb — фиксируем как событие.
        runCatching { AppMetrica.reportEvent("breadcrumb", mapOf("message" to message)) }
    }

    override fun recordException(throwable: Throwable, context: Map<String, String>) {
        val message = buildString {
            append(throwable.message ?: throwable::class.java.simpleName)
            if (context.isNotEmpty()) append(" | ").append(context.entries.joinToString())
        }
        runCatching { AppMetrica.reportError(message, throwable) }
    }

    override fun setUserId(id: String) {
        runCatching { AppMetrica.setUserProfileID(id) }
    }

    override fun setCustomKey(key: String, value: String) {
        // В AppMetrica нет произвольных ключей отчёта — игнорируем (см. Crashlytics).
    }
}
