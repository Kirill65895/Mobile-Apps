package com.example.fitnesstracker.feature.vacancy.impl.di

import android.content.Context
import com.example.fitnesstracker.core.navigation.BottomNavItem
import com.example.fitnesstracker.core.navigation.BottomNavProvider
import com.example.fitnesstracker.core.navigation.FeatureEntry
import com.example.fitnesstracker.core.navigation.NavRoutes
import com.example.fitnesstracker.feature.vacancy.api.domain.repository.VacancyAnalysisRepository
import com.example.fitnesstracker.feature.vacancy.impl.BuildConfig
import com.example.fitnesstracker.feature.vacancy.impl.data.VacancyAnalysisRepositoryImpl
import com.example.fitnesstracker.feature.vacancy.impl.data.network.AuthInterceptor
import com.example.fitnesstracker.feature.vacancy.impl.data.network.GigaChatApi
import com.example.fitnesstracker.feature.vacancy.impl.data.network.GigaChatAuthKey
import com.example.fitnesstracker.feature.vacancy.impl.data.network.GigaChatConstants
import com.example.fitnesstracker.feature.vacancy.impl.data.network.GigaChatTokenManager
import com.example.fitnesstracker.feature.vacancy.impl.data.network.GigaOAuthApi
import com.example.fitnesstracker.feature.vacancy.impl.data.network.applyGigaChatSsl
import com.example.fitnesstracker.feature.vacancy.impl.ui.VacancyFeatureEntry
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class VacancyBindingsModule {
    @Binds
    abstract fun bindRepository(impl: VacancyAnalysisRepositoryImpl): VacancyAnalysisRepository

    @Binds
    @IntoSet
    abstract fun bindFeatureEntry(entry: VacancyFeatureEntry): FeatureEntry
}

@Module
@InstallIn(SingletonComponent::class)
internal object VacancyNetworkModule {

    @Provides @Singleton
    fun provideAuthKey(): GigaChatAuthKey = GigaChatAuthKey(BuildConfig.GIGACHAT_AUTH_KEY)

    // Клиент авторизации (без интерцептора — иначе циклическая зависимость).
    @Provides @Singleton @Named("oauth")
    fun provideOAuthClient(@ApplicationContext context: Context): OkHttpClient =
        OkHttpClient.Builder().applyGigaChatSsl(context).build()

    @Provides @Singleton
    fun provideOAuthApi(@Named("oauth") client: OkHttpClient): GigaOAuthApi =
        Retrofit.Builder()
            .baseUrl(GigaChatConstants.OAUTH_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GigaOAuthApi::class.java)

    // Основной клиент: интерцептор сам добавляет токен и обновляет его при 401.
    @Provides @Singleton @Named("api")
    fun provideApiClient(
        @ApplicationContext context: Context,
        tokenManager: GigaChatTokenManager,
    ): OkHttpClient = OkHttpClient.Builder()
        .applyGigaChatSsl(context)
        .addInterceptor(AuthInterceptor { tokenManager.getValidToken() })
        .build()

    @Provides @Singleton
    fun provideGigaChatApi(@Named("api") client: OkHttpClient): GigaChatApi =
        Retrofit.Builder()
            .baseUrl(GigaChatConstants.API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GigaChatApi::class.java)

    @Provides @IntoSet
    fun provideBottomNav(): BottomNavProvider = object : BottomNavProvider {
        override val item = BottomNavItem(NavRoutes.VACANCY, "Вакансии", order = 4)
    }
}
