pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()   // AppMetrica (io.appmetrica.analytics:analytics) и большинство SDK — здесь
        // Резервный репозиторий AppMetrica/Yandex. Нужен, если артефакт не найден
        // в Maven Central или при подключении реальных Yandex LoginSDK / MapKit.
        maven { url = uri("https://maven.appmetrica.yandex.ru/") }
    }
}

rootProject.name = "FitnessTracker"

include(":app")

// ---- core ----
include(":core:common")
include(":core:ui")
include(":core:navigation")
include(":core:database")

// ---- features (combined: api/impl split) ----
include(":feature:workouts:api")
include(":feature:workouts:impl")
include(":feature:statistics:api")
include(":feature:statistics:impl")

// ---- Лабораторная №6: сторонние сервисы (за фасадами) ----
include(":core:analytics")   // AppMetrica за интерфейсом AnalyticsService
include(":core:security")    // EncryptedSharedPreferences за интерфейсом SecureStorage
include(":feature:auth:api")
include(":feature:auth:impl")
include(":feature:about")    // раздел «О нас» + карта (одномодульная фича)
