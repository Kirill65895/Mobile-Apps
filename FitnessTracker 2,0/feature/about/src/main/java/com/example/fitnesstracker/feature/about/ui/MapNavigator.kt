package com.example.fitnesstracker.feature.about.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import javax.inject.Inject

/**
 * ФАСАД над картами. Скрывает способ построения маршрута. Текущая реализация
 * открывает системное картографическое приложение и строит маршрут ОТ ТЕКУЩЕГО
 * местоположения пользователя ДО офиса — это работает без API-ключа на любом
 * устройстве. Встраиваемую карту (Google/Yandex SDK) можно подключить заменой
 * этой реализации и компонента карты на экране (см. README_LR6).
 */
interface MapNavigator {
    fun buildRoute(context: Context, office: OfficeInfo)
}

internal class IntentMapNavigator @Inject constructor() : MapNavigator {
    override fun buildRoute(context: Context, office: OfficeInfo) {
        val lat = office.latitude
        val lng = office.longitude
        // 1) Яндекс.Карты: маршрут от текущего местоположения (~) до офиса
        val yandex = Intent(Intent.ACTION_VIEW,
            Uri.parse("yandexmaps://maps.yandex.ru/?rtext=~$lat,$lng&rtt=auto"))
        // 2) Google Maps: пошаговая навигация от текущего местоположения
        val google = Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=$lat,$lng"))
        // 3) Любое картографическое приложение по geo-схеме
        val geo = Intent(Intent.ACTION_VIEW,
            Uri.parse("geo:$lat,$lng?q=$lat,$lng(${Uri.encode(office.companyName)})"))

        for (intent in listOf(yandex, google, geo)) {
            try {
                context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                return
            } catch (_: ActivityNotFoundException) {
                // пробуем следующий вариант
            }
        }
    }
}
