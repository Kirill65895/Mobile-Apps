package com.example.fitnesstracker.core.remoteconfig

import com.example.fitnesstracker.core.common.RemoteConfigService
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

/**
 * Реализация фасада Remote Config поверх Firebase. Единственное место, где
 * упоминается Firebase Remote Config. Все обращения защищены: при отсутствии
 * сети/конфигурации возвращаются значения по умолчанию из remote_config_defaults.xml.
 */
@Singleton
internal class FirebaseRemoteConfigService @Inject constructor() : RemoteConfigService {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val _updates = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    private val remoteConfig by lazy {
        Firebase.remoteConfig.apply {
            setConfigSettingsAsync(
                remoteConfigSettings {
                    // Для отладки — мгновенная загрузка; в релизе — не чаще раза в час.
                    minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) 0 else 3600
                }
            )
            setDefaultsAsync(R.xml.remote_config_defaults)
        }
    }

    override fun start() {
        scope.launch { refresh() }
        // Real-time Remote Config: мгновенные обновления без ожидания fetch-интервала.
        runCatching {
            remoteConfig.addOnConfigUpdateListener(object :
                com.google.firebase.remoteconfig.ConfigUpdateListener {
                override fun onUpdate(configUpdate: com.google.firebase.remoteconfig.ConfigUpdate) {
                    remoteConfig.activate().addOnCompleteListener { _updates.tryEmit(Unit) }
                }
                override fun onError(error: com.google.firebase.remoteconfig.FirebaseRemoteConfigException) { /* игнор */ }
            })
        }
    }

    override suspend fun refresh() {
        runCatching {
            suspendCancellableCoroutine { cont ->
                remoteConfig.fetchAndActivate()
                    .addOnCompleteListener { cont.resume(Unit) }
            }
            _updates.tryEmit(Unit)
        }
    }

    override fun getString(key: String): String =
        runCatching { remoteConfig.getString(key) }.getOrDefault("")

    override fun getBoolean(key: String): Boolean =
        runCatching { remoteConfig.getBoolean(key) }.getOrDefault(false)

    override fun updates(): Flow<Unit> = _updates.asSharedFlow()
}
