package com.example.weather

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class WeatherApp : Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(BuildConfig.MAPKIT_API_KEY)
    }
}