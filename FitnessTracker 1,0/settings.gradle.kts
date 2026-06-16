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
        mavenCentral()
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
