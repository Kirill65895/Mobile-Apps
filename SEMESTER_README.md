# Семестровая работа — «Fitness Tracker» (соответствие критериям)

Мобильное приложение на Jetpack Compose, многомодульная чистая архитектура.
Ниже — где в проекте выполнен каждый критерий.

> Выбор варианта сборки: в Android Studio → панель **Build Variants** выберите, например,
> `proDebug` (Pro-версия с ИИ) или `freeDebug` (Free-версия без ИИ).

## Обязательные критерии (30 б)

### Чистая архитектура — 8 б
- Слои: `domain` (модели, UseCase'ы, интерфейсы репозиториев в `:*:api`), `data`
  (реализации репозиториев, Room, сеть), `presentation` (Compose + ViewModel в `:*:impl`).
- 22 Gradle-модуля (`:app`, `:core:*`, `:feature:*:api/impl`).
- UseCase'ы: `GetWorkoutsUseCase`, `AddWorkoutUseCase`, `DeleteWorkoutUseCase`.
- Репозитории + маппинг DTO→Domain: `WorkoutRepositoryImpl` (Room entity → `Workout`),
  `VacancyAnalysisRepositoryImpl` (GigaChat DTO → `AnalysisResult`),
  `ProfileRepositoryImpl` (Firestore → `UserProfile`).
- Зависимости направлены внутрь: фичи общаются только через `:api`; проверяется Konsist-тестами.
- DI: **Hilt** во всех модулях.

### Фоновые задачи и сервисы — 6 б
- **WorkManager**: `WorkoutReminderWorker` (периодическая задача, раз в сутки) с
  **Constraints** (`setRequiresBatteryNotLow`), планируется в `ReminderScheduler`
  (`app/work/`), запускается из `FitnessApp.onCreate`.
- **Service**: `PushMessagingService` (FCM) — `app/fcm/`.
- **BroadcastReceiver**: `BootReceiver` — перепланирует задачу после перезагрузки
  (`RECEIVE_BOOT_COMPLETED`).

### Анимации в Jetpack Compose — 4 б
Экран «Анализ вакансии» (`VacancyScreen`):
- **Crossfade** — плавная смена состояний Idle/Loading/Error/Success.
- **AnimatedVisibility** (`fadeIn` + `expandVertically`) — появление карточки результата.
- **animateContentSize** — плавное изменение высоты под объём ответа.

### XML разметка и интеграция Compose — 4 б
Экран «О нас» (`AboutScreen`): классический `RatingBar` из XML-разметки
(`res/layout/view_app_rating.xml`) встроен в Compose через **`AndroidView`**.
Данные передаются в обе стороны: Compose-состояние → View и обратно через
`OnRatingBarChangeListener`. Обоснование: `RatingBar` — зрелый View-виджет, демонстрирует
постепенную интеграцию/переиспользование View внутри Compose.

### Gradle: конфигурация сборок — 4 б
- **buildTypes**: `debug` (suffix `.debug`) и `release` (**R8**: `isMinifyEnabled` +
  `isShrinkResources` + `proguard-rules.pro`).
- **2 productFlavors** (`free`, `pro`) с реальными отличиями:
  - разный `applicationId` (`…​.free` / `…​.pro`),
  - разное имя приложения (`Fitness Tracker Free` / `Pro` через `resValue`),
  - **feature-флаг** `BuildConfig.IS_PRO` → в Free ИИ-анализ вакансий заблокирован,
    в Pro доступен (`FeatureConfig` + `VacancyViewModel`).

### Качество кода и UX — 4 б
Осмысленный сценарий (учёт тренировок + статистика + профиль + ИИ-анализ),
состояния загрузки/ошибок (sealed UiState), обработка сетевых ошибок, отсутствие крашей
(все внешние вызовы в `runCatching`, degraded-режим без ключей).

## Бонусы (до +10 б)
- **Firebase (+3)**: FCM (push), Remote Config (баннер/флаг), Firestore (профиль) — все 3.
- **ИИ (+3)**: GigaChat — анализ вакансий с учётом навыков (OAuth 2.0, кеш токена,
  Interceptor), органично встроен как отдельный сценарий.
- **Внешний сервис (+2)**: авторизация VK/Yandex/Google + Yandex AppMetrica + карта (Intent).
- **Крашлитика и аналитика (+1)**: Firebase Crashlytics + AppMetrica; кастомные события
  (`workout_added`, `screen_viewed`) и handled-ошибки (`recordException`).
- Публикация в магазин — не выполнялась.

Ядро приложения (тренировки + статистика на Room) работает полностью офлайн без ключей.
Внешние интеграции готовы и включаются добавлением реальных учётных данных:
- `local.properties`: `appmetrica_api_key=…`, `gigachat_auth_key=…`.
- `app/google-services.json` — заменить заглушку на файл из консоли Firebase
  (в нём уже учтены applicationId с суффиксами `.free`/`.pro`/`.debug`).
- `feature/vacancy/impl/src/main/res/raw/russian_trusted_root_ca.cer` — реальный
  сертификат Минцифры для GigaChat.
- Реальные SDK VK/Yandex/Google вместо демо-провайдеров (точки замены — в комментариях).
