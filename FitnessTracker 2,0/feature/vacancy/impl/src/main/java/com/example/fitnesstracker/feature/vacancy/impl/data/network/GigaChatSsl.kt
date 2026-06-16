package com.example.fitnesstracker.feature.vacancy.impl.data.network

import android.content.Context
import com.example.fitnesstracker.feature.vacancy.impl.R
import okhttp3.OkHttpClient
import java.security.KeyStore
import java.security.cert.CertificateFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

/**
 * Подключение российского корневого сертификата для API GigaChat. Сертификат
 * нужно скачать с сайта Минцифры/из документации GigaChat и положить в
 * res/raw/russian_trusted_root_ca.cer (сейчас там заглушка).
 *
 * Если сертификат не настроен — возвращаем клиент как есть (без падения сборки/рантайма).
 */
internal fun OkHttpClient.Builder.applyGigaChatSsl(context: Context): OkHttpClient.Builder {
    return runCatching {
        val cf = CertificateFactory.getInstance("X.509")
        val cert = context.resources.openRawResource(R.raw.russian_trusted_root_ca).use {
            cf.generateCertificate(it)
        }
        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType()).apply {
            load(null, null)
            setCertificateEntry("russian_trusted_root_ca", cert)
        }
        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
            init(keyStore)
        }
        val trustManager = tmf.trustManagers.first { it is X509TrustManager } as X509TrustManager
        val sslContext = SSLContext.getInstance("TLS").apply {
            init(null, arrayOf(trustManager), null)
        }
        sslSocketFactory(sslContext.socketFactory, trustManager)
        this
    }.getOrDefault(this)   // заглушка/ошибка сертификата → клиент без кастомного SSL
}
