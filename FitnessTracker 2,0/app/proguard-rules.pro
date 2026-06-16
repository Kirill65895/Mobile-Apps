# --- Лабораторная №8: читаемые stacktrace из release-сборки (R8) ---

# Сохраняем имя исходного файла и таблицу строк — без этого сервис восстановит
# имя класса/метода, но потеряет точную строку Kotlin-файла.
-keepattributes SourceFile,LineNumberTable
# Скрываем реальные имена файлов в стеке, оставляя сопоставление через mapping.txt.
-renamesourcefileattribute SourceFile

# Сохраняем осмысленные имена пользовательских исключений (видно InvalidWorkoutException,
# а не a.b.c) — полезно для группировки ошибок в Crashlytics/AppMetrica.
-keep public class * extends java.lang.Exception

# Firebase, Hilt, Room, AppMetrica и Compose поставляют собственные consumer-правила,
# поэтому дублировать их здесь не требуется. Добавляйте точечные -keep ТОЛЬКО для
# классов, используемых через reflection/сериализацию.

# --- Лабораторная №9: GigaChat ---
# Gson сериализует DTO через reflection — точечно сохраняем модели сети.
-keep class com.example.fitnesstracker.feature.vacancy.impl.data.network.** { *; }
-keepattributes Signature, *Annotation*
# Retrofit и OkHttp поставляют собственные consumer-правила — дублировать не нужно.
