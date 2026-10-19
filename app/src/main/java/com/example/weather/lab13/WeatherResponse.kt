package com.example.weather.lab13

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    @SerializedName("current_weather")
    val currentWeather: CurrentWeather?
)

data class CurrentWeather(
    val temperature: Double,
    val windSpeed: Double,
    val windDirection: Double,
    val weatherCode: Int,
    val time: String
)
