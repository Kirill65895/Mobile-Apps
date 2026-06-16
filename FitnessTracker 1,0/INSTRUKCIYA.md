# Fitness Tracker — многомодульное приложение (Лабораторная работа №5)

Приложение для учёта тренировок. Архитектура — **Clean Architecture + многомодульность**.
Основная (главная) ветка реализует **комбинированный подход Google** (combined):
слои + фичи + разделение каждой фичи на `:api` / `:impl`.

---

## 0. Версии (Android Studio 2026.1.1)

В каталоге версий `gradle/libs.versions.toml` заданы:

| Инструмент | Версия |
|---|---|
| Android Gradle Plugin | 8.7.3 |
| Gradle wrapper | 8.11.1 |
| Kotlin | 2.0.21 |
| KSP | 2.0.21-1.0.28 |
| Hilt | 2.52 |
| compileSdk / targetSdk | 35 |
| minSdk | 26 |

> Это стабильный, проверенный набор версий: он открывается и собирается в
> Android Studio 2026.1.1 без миграции на новый DSL AGP 9. Все версии правятся
> в ОДНОМ месте — `libs.versions.toml` (критерий №4).
>
> **Если нужен именно AGP 9.x** (новый встроенный Kotlin / новый DSL): после
> успешной сборки запустите **Tools → AGP Upgrade Assistant** в Android Studio —
> он сам переведёт проект на AGP 9 (Gradle 9.1.0, удалит плагин
> `org.jetbrains.kotlin.android`, перепишет DSL). Делать это вручную не нужно.

---

## 1. Создаём пустой проект

В Android Studio: **New Project → No Activity** (или *Empty Activity* и потом удалить
сгенерированный код). Параметры:
- Name: `FitnessTracker`
- Package: `com.example.fitnesstracker`
- Language: Kotlin, Build: Kotlin DSL (`build.gradle.kts`)
- Minimum SDK: API 26

После создания у вас уже есть: `gradlew`, `gradle/wrapper/gradle-wrapper.jar`,
`gradle/wrapper/gradle-wrapper.properties`, `local.properties`, `.gitignore`.
**Их НЕ трогаем** (jar и local.properties берём из сгенерированного проекта).

---

## 2. Порядок добавления файлов

Добавляйте строго **снизу вверх по графу зависимостей** — тогда каждый модуль
компилируется, когда уже готовы те, от кого он зависит.

### Шаг 1 — корневые файлы (заменяют сгенерированные)
1. `settings.gradle.kts`
2. `build.gradle.kts` (корневой)
3. `gradle.properties`
4. `gradle/libs.versions.toml`
5. `gradle/wrapper/gradle-wrapper.properties` (только строку `distributionUrl` — версия Gradle)

### Шаг 2 — модуль `:core:common` (чистый Kotlin, без Android)
`core/common/build.gradle.kts` + папка `src/main/java/.../core/common/` целиком.

### Шаг 3 — `:core:navigation` и `:core:ui`
Папки `core/navigation/` и `core/ui/` целиком.

### Шаг 4 — `:core:database`
Папка `core/database/` целиком.

### Шаг 5 — `:feature:workouts:api` (domain-контракт, чистый Kotlin)
Папка `feature/workouts/api/` целиком.

### Шаг 6 — `:feature:workouts:impl` (data + ui + di)
Папка `feature/workouts/impl/` целиком.

### Шаг 7 — `:feature:statistics:api`, затем `:feature:statistics:impl`
Папки `feature/statistics/api/` и `feature/statistics/impl/` целиком.

### Шаг 8 — модуль `:app`
Папка `app/` целиком (включая `AndroidManifest.xml`, `res/`, исходники и
`app/src/test/.../konsist/ArchitectureTest.kt`).

### Шаг 9 — синхронизация
**File → Sync Project with Gradle Files**, затем **Run 'app'**.

---

## 3. Граф зависимостей модулей (combined)

```
                         :app  (точка входа, композитор)
                          │  знает обо всех модулях, строит DI-граф и NavHost
        ┌─────────────────┼───────────────────────┐
        ▼                 ▼                         ▼
:feature:workouts:impl   :feature:statistics:impl   :core:* (ui, navigation, database, common)
        │                 │  
        ▼                 ▼  
:feature:workouts:api ◄── :feature:statistics:api      (фичи общаются ТОЛЬКО через :api)
        │
        ▼
   :core:common  (чистый Kotlin — нет Android-зависимостей)
```

Ключевые свойства:
- `:*:api` фич — **чистые Kotlin-модули** → domain структурно не зависит от Android.
- Интерфейс репозитория — в `:api` (domain), реализация — в `:impl` (data).
- `statistics:impl` зависит от `workouts:api`, но **не** от `workouts:impl`.
- Навигация между фичами — через интерфейс `FeatureEntry` из `:core:navigation`,
  который каждая фича отдаёт в Hilt через `@IntoSet`. `:app` собирает `Set<FeatureEntry>`
  и строит `NavHost`, не зная фич поимённо (критерий №7.1).

---

## 4. Git: три ветки (критерии №5–8)

Сначала зафиксируйте базовую структуру:

```bash
git init
git add .
git commit -m "feat: базовый многомодульный проект :app + :core (combined)"

git branch layer-based
git branch feature-based
git branch combined
```

Текущая структура репозитория **= ветка `combined`** (подход Google, критерий №8).
В неё уже входит:
- слои фич (domain/data/ui) + разделение на `:api`/`:impl`;
- переиспользование общего источника данных `:core:database`;
- переиспользование domain-контрактов между фичами (`statistics` → `workouts:api`).

Просто переключитесь и зафиксируйте:
```bash
git switch combined
git commit --allow-empty -m "docs: combined = слои + фичи + api/impl (Google)"
```

### Ветка `layer-based` (критерий №6) — модули по слоям
```bash
git switch layer-based
```
Структура `settings.gradle.kts`:
```kotlin
include(":app")
include(":core")          // объединяет common+ui+navigation
include(":domain")        // ВСЕ модели, интерфейсы репозиториев и usecase'ы
include(":data")          // Room + реализации репозиториев
include(":ui")            // все Compose-экраны и ViewModel'и
```
Перенос кода (тот же самый, меняются только границы модулей):
- `feature/*/api/.../domain/**` и `core/database/Workout*` (модели) → `:domain`
- `core/database/**` (Room) + `feature/*/impl/.../data/**` → `:data`
- `feature/*/impl/.../ui/**` + `app/AppRoot,MainActivity` → `:ui`
- `:domain` — чистый Kotlin-модуль (без Android).
Зависимости: `:ui → :domain`, `:data → :domain`, `:app → :ui,:data,:core`.

### Ветка `feature-based` (критерии №7, 7.1) — модули по фичам
```bash
git switch feature-based
```
Структура:
```kotlin
include(":app")
include(":core:common", ":core:ui", ":core:navigation", ":core:database")
include(":feature:workouts")     // ОДИН модуль на фичу, внутри пакеты domain/data/ui/di
include(":feature:statistics")
```
То есть берём combined и **схлопываем** `:api`+`:impl` каждой фичи в один модуль
`:feature:<name>` (пакеты `...domain`, `...data`, `...ui`, `...di` остаются).
DI (Hilt) и `:core:navigation` с `FeatureEntry` уже настроены — это и есть пункт 7.1:
навигация между фичами без прямых зависимостей.

### Ветка `combined` — итоговая (критерий №8)
Это `main`/текущая структура. Возврат:
```bash
git switch combined
```

> Konsist-тесты (критерий №9) лежат в `:app` и работают во всех трёх ветках —
> правила сформулированы по именам пакетов (`..domain..`, `..data..`, `feature.*`),
> а не по именам модулей.

---

## 5. Запуск архитектурных тестов (Konsist, критерий №9)

```bash
./gradlew :app:testDebugUnitTest
```
Проверяются 5 правил:
1. domain не импортирует `android*`;
2. data не импортирует Compose/Navigation (UI);
3. фичи не зависят от `*.impl` чужих фич;
4. классы `*UseCase` лежат в `..domain..`;
5. `*Repository` — интерфейсы в `..domain..`, `*RepositoryImpl` — в `..data..`.

---

## 6. Соответствие критериям ЛР №5

| № | Критерий | Где реализовано |
|---|---|---|
| 1 | Тема | Fitness Tracker |
| 2 | Domain: модели, интерфейсы репозиториев, usecase'ы | `feature/*/api/.../domain/**` |
| 3 | UI и Data слои | `feature/*/impl/.../ui`, `.../data` |
| 4 | `:app` + `:core`, version catalog, единые версии | `settings.gradle.kts`, `gradle/libs.versions.toml`, `:core:*` |
| 5 | 3 ветки | `git branch layer-based feature-based combined` |
| 6 | layer-based: data/domain/ui модулями | ветка `layer-based` |
| 7 | feature-based: модули-фичи | ветка `feature-based` |
| 7.1 | Hilt в многомодульности + `:core:navigation` без прямых связей | `*/di/*Module.kt`, `core/navigation/FeatureEntry.kt`, `app/MainActivity.kt` |
| 8 | combined (Google) | ветка `combined` (текущая) |
| 9 | Konsist-тесты архитектуры | `app/src/test/.../konsist/ArchitectureTest.kt` |
