plugins {
    alias(libs.plugins.kotlin.jvm)
}
// :feature:profile:api — чистый Kotlin/JVM контракт фичи профиля (без Android, без Firebase).
dependencies {
    implementation(project(":core:common"))
    api(libs.kotlinx.coroutines.core)
}
