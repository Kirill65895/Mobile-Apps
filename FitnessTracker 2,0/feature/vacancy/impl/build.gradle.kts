import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

// Авторизационный ключ GigaChat читаем из local.properties (НЕ из кода и НЕ из git).
val localProps = Properties().apply {
    val f = rootProject.file("local.properties")
    if (f.exists()) f.inputStream().use { load(it) }
}
val gigachatKey: String = (localProps["gigachat_auth_key"] as String?) ?: ""

android {
    namespace = "com.example.fitnesstracker.feature.vacancy.impl"
    compileSdk = 35
    defaultConfig {
        minSdk = 26
        // Ключ доступен как BuildConfig.GIGACHAT_AUTH_KEY (пустой, если не задан → демо-режим).
        buildConfigField("String", "GIGACHAT_AUTH_KEY", "\"$gigachatKey\"")
    }
    buildFeatures { compose = true; buildConfig = true }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}

dependencies {
    implementation(project(":feature:vacancy:api"))
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:navigation"))
    implementation(project(":core:security"))   // EncryptedSharedPreferences для токена

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)
}
