package com.example.weather.lab19.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CityRepository(private val localDataSource: CityLocalDataSource) {
    private val _cities = MutableLiveData<List<City>>()
    val cities: LiveData<List<City>> = _cities

    suspend fun loadCities(): List<City> = withContext(Dispatchers.IO) {
        val cities = localDataSource.getAll()
        _cities.postValue(cities)
        cities
    }

    suspend fun addCity(name: String, description: String) = withContext(Dispatchers.IO) {
        localDataSource.add(City(name = name, description = description))
        _cities.postValue(localDataSource.getAll())
    }

    suspend fun updateCity(city: City, name: String, description: String) = withContext(Dispatchers.IO) {
        localDataSource.update(city.copy(name = name, description = description))
        _cities.postValue(localDataSource.getAll())
    }

    suspend fun deleteCity(city: City) = withContext(Dispatchers.IO) {
        localDataSource.delete(city)
        _cities.postValue(localDataSource.getAll())
    }

    fun close() = localDataSource.close()
}
