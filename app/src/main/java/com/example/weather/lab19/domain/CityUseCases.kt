package com.example.weather.lab19.domain

import com.example.weather.lab19.data.City
import com.example.weather.lab19.data.CityRepository

class CityUseCases(private val repository: CityRepository) {
    val cities = repository.cities

    suspend fun load() = repository.loadCities()

    suspend fun add(name: String, description: String) {
        repository.addCity(name.trim(), description.trim())
    }

    suspend fun update(city: City, name: String, description: String) {
        repository.updateCity(city, name.trim(), description.trim())
    }

    suspend fun delete(city: City) = repository.deleteCity(city)

    fun close() = repository.close()
}
