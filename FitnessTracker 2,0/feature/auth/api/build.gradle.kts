plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}
// :feature:auth:api — публичный контракт фичи авторизации.
// Это android-library (а не чистый Kotlin), потому что вход через SDK требует
// Activity. Контракт и модели — собственные, без типов из SDK.
android {
    namespace = "com.example.fitnesstracker.feature.auth.api"
    compileSdk = 35
    defaultConfig { minSdk = 26 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}
dependencies {
    api(libs.kotlinx.coroutines.core)
}
