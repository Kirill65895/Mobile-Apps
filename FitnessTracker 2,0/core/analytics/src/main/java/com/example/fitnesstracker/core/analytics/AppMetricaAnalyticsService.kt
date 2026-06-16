package com.example.fitnesstracker.core.analytics

import com.example.fitnesstracker.core.common.AnalyticsService
import io.appmetrica.analytics.AppMetrica
import javax.inject.Inject

/**
 * Реализация фасада аналитики поверх AppMetrica SDK. Это ЕДИНСТВЕННОЕ место в
 * проекте, где упоминается класс AppMetrica. Все вызовы обёрнуты в try/catch и
 * проверку активации, чтобы отсутствие ключа никогда не приводило к падению.
 */
internal class AppMetricaAnalyticsService @Inject constructor(
    private val initializer: AnalyticsInitializer,
) : AnalyticsService {

    override fun trackEvent(name: String, params: Map<String, Any>) {
        if (!initializer.activated) return
        runCatching {
            if (params.isEmpty()) {
                AppMetrica.reportEvent(name)
            } else {
                AppMetrica.reportEvent(name, params.mapValues { it.value.toString() })
            }
        }
    }

    override fun trackError(message: String, error: Throwable?) {
        if (!initializer.activated) return
        runCatching { AppMetrica.reportError(message, error) }
    }
}
