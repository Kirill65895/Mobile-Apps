plugins {
    alias(libs.plugins.kotlin.jvm)
}
// :feature:workouts:api — ЧИСТЫЙ Kotlin/JVM модуль (domain-контракт фичи).
// Android-зависимостей нет ПО ОПРЕДЕЛЕНИЮ → правило "domain не зависит от Android"
// выполняется структурно. Другие фичи зависят ТОЛЬКО от этого :api модуля.
dependencies {
    implementation(project(":core:common"))
    api(libs.kotlinx.coroutines.core)
    implementation(libs.javax.inject)
}
