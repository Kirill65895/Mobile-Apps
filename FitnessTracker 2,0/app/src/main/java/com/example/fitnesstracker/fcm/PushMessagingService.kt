package com.example.fitnesstracker.fcm

import android.util.Log
import com.example.fitnesstracker.core.navigation.NavRoutes
import com.example.fitnesstracker.core.security.SecureStorage
import com.example.fitnesstracker.feature.auth.api.AuthService
import com.example.fitnesstracker.feature.profile.api.domain.repository.ProfileRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Сервис обработки FCM-событий. Зарегистрирован в манифесте с action
 * com.google.firebase.MESSAGING_EVENT и exported="false".
 *
 * Foreground и Data-сообщения приходят в onMessageReceived(); Notification-сообщения
 * в фоне рисует сама система. Здесь мы всегда создаём уведомление сами (из data),
 * чтобы поведение было одинаковым.
 */
@AndroidEntryPoint
class PushMessagingService : FirebaseMessagingService() {

    @Inject lateinit var notificationHelper: NotificationHelper
    @Inject lateinit var secureStorage: SecureStorage
    @Inject lateinit var authService: AuthService
    @Inject lateinit var profileRepository: ProfileRepository

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onNewToken(token: String) {
        Log.d(TAG, "Новый FCM-токен: $token")
        // Сохраняем локально (для отправки на сервер при удобном случае).
        secureStorage.putString(KEY_FCM_TOKEN, token)
        // Если пользователь авторизован — обновляем токен в его профиле Firestore.
        authService.getCurrentUser()?.let { user ->
            scope.launch { profileRepository.updateFcmToken(user.id, token) }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val data = message.data
        val title = message.notification?.title ?: data["title"] ?: "Fitness Tracker"
        val body = message.notification?.body ?: data["body"] ?: ""
        val route = data["route"] ?: NavRoutes.PROFILE   // какой экран открыть по нажатию
        notificationHelper.show(title = title, body = body, route = route, data = data)
    }

    companion object {
        private const val TAG = "PushMessagingService"
        const val KEY_FCM_TOKEN = "fcm_token"
    }
}
