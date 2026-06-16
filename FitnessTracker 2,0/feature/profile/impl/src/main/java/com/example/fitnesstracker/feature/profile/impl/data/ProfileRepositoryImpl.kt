package com.example.fitnesstracker.feature.profile.impl.data

import com.example.fitnesstracker.feature.profile.api.domain.model.UserProfile
import com.example.fitnesstracker.feature.profile.api.domain.repository.ProfileRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Реализация ProfileRepository поверх Cloud Firestore. Единственное место, где
 * упоминается Firestore. Чтение — через addSnapshotListener (реальное время),
 * подписка корректно снимается в awaitClose (нет утечки слушателя).
 */
@Singleton
internal class ProfileRepositoryImpl @Inject constructor() : ProfileRepository {

    private val users = Firebase.firestore.collection("users")

    override suspend fun saveProfile(profile: UserProfile) {
        runCatching {
            users.document(profile.id).set(
                mapOf(
                    "id" to profile.id,
                    "name" to profile.name,
                    "email" to profile.email,
                    "fcmToken" to profile.fcmToken,
                    "updatedAtMillis" to profile.updatedAtMillis,
                )
            ).await()
        }
    }

    override suspend fun updateFcmToken(userId: String, token: String) {
        runCatching {
            users.document(userId).update(
                mapOf("fcmToken" to token, "updatedAtMillis" to System.currentTimeMillis())
            ).await()
        }
    }

    override fun observeProfile(userId: String): Flow<UserProfile?> = callbackFlow {
        val registration = users.document(userId).addSnapshotListener { snapshot, error ->
            if (error != null) { trySend(null); return@addSnapshotListener }
            val profile = snapshot?.takeIf { it.exists() }?.let {
                UserProfile(
                    id = it.getString("id") ?: userId,
                    name = it.getString("name").orEmpty(),
                    email = it.getString("email"),
                    fcmToken = it.getString("fcmToken"),
                    updatedAtMillis = it.getLong("updatedAtMillis") ?: 0L,
                )
            }
            trySend(profile)
        }
        awaitClose { registration.remove() }   // снятие подписки — нет утечки
    }
}

/** Преобразование Firebase Task в suspend-вызов без доп. зависимостей. */
private suspend fun <T> com.google.android.gms.tasks.Task<T>.await(): T =
    suspendCancellableCoroutine { cont ->
        addOnSuccessListener { cont.resume(it) }
        addOnFailureListener { cont.resumeWithException(it) }
    }
