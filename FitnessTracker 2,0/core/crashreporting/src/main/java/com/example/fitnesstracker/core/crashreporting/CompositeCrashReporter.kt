package com.example.fitnesstracker.core.crashreporting

import com.example.fitnesstracker.core.common.CrashReporter
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Композит: рассылает каждое событие во все подключённые сервисы крашлитики.
 * Именно его получает остальной код через интерфейс CrashReporter. Чтобы
 * добавить/убрать сервис, достаточно изменить набор провайдеров здесь.
 */
@Singleton
internal class CompositeCrashReporter @Inject constructor(
    private val crashlytics: CrashlyticsCrashReporter,
    private val appMetrica: AppMetricaCrashReporter,
) : CrashReporter {

    private val reporters = listOf(crashlytics, appMetrica)

    override fun log(message: String) = reporters.forEach { it.log(message) }
    override fun recordException(throwable: Throwable, context: Map<String, String>) =
        reporters.forEach { it.recordException(throwable, context) }
    override fun setUserId(id: String) = reporters.forEach { it.setUserId(id) }
    override fun setCustomKey(key: String, value: String) =
        reporters.forEach { it.setCustomKey(key, value) }
}
