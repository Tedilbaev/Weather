package com.example.weather.lab19.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.weather.lab19.data.City
import com.example.weather.lab19.data.CityLocalDataSource
import com.example.weather.lab19.data.CityRepository
import com.example.weather.lab19.domain.CityUseCases
import kotlinx.coroutines.launch

class CityListViewModel(application: Application) : AndroidViewModel(application) {
    private val useCases = CityUseCases(
        CityRepository(CityLocalDataSource(application))
    )

    val cities: LiveData<List<City>> = useCases.cities

    init {
        viewModelScope.launch {
            val loadedCities = useCases.load()
            seedIfEmpty(loadedCities.isEmpty())
        }
    }

    private suspend fun seedIfEmpty(isEmpty: Boolean) {
        if (isEmpty) {
            useCases.add("Москва", "Столица, переменная облачность")
            useCases.add("Санкт-Петербург", "Дожди, +8°")
            useCases.add("Казань", "Ясно, +12°")
        }
    }

    fun addCity(name: String, description: String) {
        if (name.isBlank()) return
        viewModelScope.launch { useCases.add(name, description) }
    }

    fun updateCity(city: City, name: String, description: String) {
        if (name.isBlank()) return
        viewModelScope.launch { useCases.update(city, name, description) }
    }

    fun deleteCity(city: City) {
        viewModelScope.launch { useCases.delete(city) }
    }

    override fun onCleared() {
        useCases.close()
        super.onCleared()
    }
}
