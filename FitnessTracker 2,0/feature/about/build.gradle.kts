plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}
// Одномодульная фича: у «О нас» нет публичного контракта для других фич,
// поэтому деление на api/impl излишне (см. README_LR6, раздел про combined).
android {
    namespace = "com.example.fitnesstracker.feature.about"
    compileSdk = 35
    defaultConfig { minSdk = 26 }
    buildFeatures { compose = true }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}
dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:navigation"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    // === Реальная карта (по умолчанию ОТКЛЮЧЕНА — требует API-ключа) ===
    // Google Maps Compose:
    // implementation("com.google.maps.android:maps-compose:6.+")
    // implementation("com.google.android.gms:play-services-maps:19.+")
    // или Yandex MapKit Lite:
    // implementation("com.yandex.android:maps.mobile:4.+")
}
