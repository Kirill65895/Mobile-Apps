plugins {
    alias(libs.plugins.kotlin.jvm)
}
// :feature:vacancy:api — чистый Kotlin/JVM контракт фичи анализа вакансий
// (без Android, без Retrofit). Реализация (GigaChat) живёт в :impl.
