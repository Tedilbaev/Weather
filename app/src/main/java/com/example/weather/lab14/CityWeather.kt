package com.example.weather.lab14

data class CityWeather(
    val cityName: String,
    val temp: Double,
    val description: String,
    val humidity: Int,
    val windSpeed: Double
)