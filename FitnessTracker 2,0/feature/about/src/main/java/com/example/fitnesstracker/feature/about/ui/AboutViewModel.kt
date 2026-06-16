package com.example.fitnesstracker.feature.about.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.fitnesstracker.core.common.AnalyticsEvents
import com.example.fitnesstracker.core.common.AnalyticsService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(
    private val analytics: AnalyticsService,
    private val mapNavigator: MapNavigator,
) : ViewModel() {

    val office: OfficeInfo = OfficeInfo.DEMO

    fun onScreenViewed() {
        analytics.trackEvent(
            AnalyticsEvents.SCREEN_VIEWED,
            mapOf(AnalyticsEvents.PARAM_SCREEN_NAME to "about"),
        )
    }

    fun onBuildRoute(context: Context) = mapNavigator.buildRoute(context, office)
}
