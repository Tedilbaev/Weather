package com.example.weather.lab19.data

import android.content.Context
import com.example.weather.lab11.WeatherDbHelper

class CityLocalDataSource(context: Context) : AutoCloseable {
    private val db = WeatherDbHelper(context.applicationContext)

    fun getAll(): List<City> = db.getAllCities().map { city ->
        City(city.id, city.name, city.description)
    }

    fun add(city: City): Long = db.addCity(city.name, city.description)

    fun update(city: City): Int = db.updateCity(city.id, city.name, city.description)

    fun delete(city: City): Int = db.deleteCity(city.id)

    override fun close() = db.close()
}
