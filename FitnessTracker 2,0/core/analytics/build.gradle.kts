import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

// Читаем API-ключ AppMetrica из local.properties (НЕ из кода и НЕ из git).
val localProps = Properties().apply {
    val f = rootProject.file("local.properties")
    if (f.exists()) f.inputStream().use { load(it) }
}
val appmetricaKey: String = (localProps["appmetrica_api_key"] as String?) ?: ""

android {
    namespace = "com.example.fitnesstracker.core.analytics"
    compileSdk = 35
    defaultConfig {
        minSdk = 26
        // Ключ попадает в BuildConfig.APPMETRICA_API_KEY (пустой, если не задан).
        buildConfigField("String", "APPMETRICA_API_KEY", "\"$appmetricaKey\"")
    }
    buildFeatures { buildConfig = true }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}

dependencies {
    implementation(project(":core:common"))
    implementation(libs.appmetrica.analytics)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}
