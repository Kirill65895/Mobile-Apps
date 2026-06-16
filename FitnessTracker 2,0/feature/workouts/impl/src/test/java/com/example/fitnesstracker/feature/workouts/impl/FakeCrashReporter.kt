package com.example.fitnesstracker.feature.workouts.impl

import com.example.fitnesstracker.core.common.CrashReporter

/** Тестовая заглушка фасада крашлитики: запоминает breadcrumbs и non-fatal ошибки. */
class FakeCrashReporter : CrashReporter {
    val logs = mutableListOf<String>()
    val exceptions = mutableListOf<Throwable>()
    override fun log(message: String) { logs += message }
    override fun recordException(throwable: Throwable, context: Map<String, String>) { exceptions += throwable }
    override fun setUserId(id: String) {}
    override fun setCustomKey(key: String, value: String) {}
}
