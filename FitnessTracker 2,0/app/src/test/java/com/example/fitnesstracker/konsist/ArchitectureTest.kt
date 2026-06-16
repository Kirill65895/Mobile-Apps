package com.example.fitnesstracker.konsist

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.verify.assertFalse
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.Test

/**
 * Архитектурные тесты на Konsist (критерий №9).
 * Запуск: ./gradlew :app:testDebugUnitTest
 */
class ArchitectureTest {

    private val featurePrefix = "com.example.fitnesstracker.feature."

    // 1) Модули domain НЕ зависят от Android-фреймворка
    @Test
    fun `domain layer does not depend on Android`() {
        Konsist.scopeFromProject()
            .files
            .filter { it.packagee?.name?.contains(".domain") == true }
            .assertFalse { file ->
                file.imports.any { it.name.startsWith("android") }
            }
    }

    // 2) Модули data НЕ зависят от UI-компонентов (Compose)
    @Test
    fun `data layer does not depend on UI`() {
        Konsist.scopeFromProject()
            .files
            .filter { it.packagee?.name?.contains(".data") == true }
            .assertFalse { file ->
                file.imports.any {
                    it.name.startsWith("androidx.compose") ||
                        it.name.startsWith("androidx.navigation")
                }
            }
    }

    // 3) Feature-модули НЕ зависят друг от друга напрямую (только через :api)
    @Test
    fun `features do not depend on other features impl`() {
        Konsist.scopeFromProject()
            .files
            .filter { it.packagee?.name?.startsWith(featurePrefix) == true }
            .assertFalse { file ->
                val ownFeature = file.packagee!!.name
                    .removePrefix(featurePrefix)
                    .substringBefore(".")
                file.imports.any { imp ->
                    imp.name.startsWith(featurePrefix) &&
                        imp.name.contains(".impl") &&
                        imp.name.removePrefix(featurePrefix).substringBefore(".") != ownFeature
                }
            }
    }

    // 4) Use cases находятся в domain-слое
    @Test
    fun `use cases reside in domain layer`() {
        Konsist.scopeFromProject()
            .classes()
            .withNameEndingWith("UseCase")
            .assertTrue { it.resideInPackage("..domain..") }
    }

    // 5) Репозитории — интерфейсы в domain, реализация в data
    @Test
    fun `repositories are interfaces in domain`() {
        Konsist.scopeFromProject()
            .interfaces()
            .withNameEndingWith("Repository")
            .assertTrue { it.resideInPackage("..domain..") }
    }

    @Test
    fun `repository implementations reside in data layer`() {
        Konsist.scopeFromProject()
            .classes()
            .withNameEndingWith("RepositoryImpl")
            .assertTrue { it.resideInPackage("..data..") }
    }
}
