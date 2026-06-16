plugins {
    alias(libs.plugins.kotlin.jvm)
}
// :core:common — чистый Kotlin/JVM модуль. БЕЗ зависимостей от Android.
// Содержит общие утилиты, базовые обёртки и константы (критерий №4).
dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
