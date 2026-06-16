plugins {
    alias(libs.plugins.kotlin.jvm)
}
// :feature:statistics:api — чистый Kotlin domain-контракт фичи статистики.
// Зависит от :feature:workouts:api (контракт другой фичи) — это допустимо:
// фичи общаются ТОЛЬКО через :api модули, без зависимостей на :impl.
dependencies {
    implementation(project(":core:common"))
    api(project(":feature:workouts:api"))
    api(libs.kotlinx.coroutines.core)
    implementation(libs.javax.inject)
}
