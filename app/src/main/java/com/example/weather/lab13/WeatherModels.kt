package com.example.weather.lab13

data class OwmResponse(
    val name: String,
    val main: Main,
    val wind: Wind,
    val weather: List<WeatherItem>
)

data class Main(
    val temp: Double,
    val humidity: Int,
    val pressure: Int
)

data class Wind(
    val speed: Double
)

data class WeatherItem(
    val main: String,
    val description: String
)