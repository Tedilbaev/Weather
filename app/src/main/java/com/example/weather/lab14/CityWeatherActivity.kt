package com.example.weather.lab14

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.BuildConfig
import com.example.weather.R
import com.example.weather.lab11.WeatherDbHelper
import com.example.weather.lab13.OwmResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CityWeatherActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "Lab14_Weather"
        private val apiKey: String
            get() = BuildConfig.OWM_API_KEY
    }

    private lateinit var db: WeatherDbHelper
    private lateinit var adapter: CityWeatherAdapter
    private val results = mutableListOf<CityWeather>()
    private var pendingRequests = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lab14_activity_city_weather)

        db = WeatherDbHelper(this)
        adapter = CityWeatherAdapter()
        findViewById<RecyclerView>(R.id.rvCities).adapter = adapter

        seedCitiesIfNeeded()

        findViewById<Button>(R.id.btnLoad).setOnClickListener {
            loadWeatherForAllCities()
        }
    }

    private fun seedCitiesIfNeeded() {
        if (db.getAllCities().isEmpty()) {
            db.addCity("Moscow", "Москва")
            db.addCity("Saint Petersburg", "Санкт-Петербург")
            db.addCity("Kazan", "Казань")
            db.addCity("Novosibirsk", "Новосибирск")
            db.addCity("Yekaterinburg", "Екатеринбург")
        }
    }

    private fun loadWeatherForAllCities() {
        val cities = db.getAllCities()
        if (cities.isEmpty()) {
            Toast.makeText(this, R.string.lab14_no_cities, Toast.LENGTH_SHORT).show()
            return
        }

        results.clear()
        adapter.submitList(emptyList())
        pendingRequests = cities.size

        Toast.makeText(this, getString(R.string.lab14_loading, cities.size), Toast.LENGTH_SHORT).show()

        cities.forEach { city ->
            // q = name (латиница для OWM)
            RetrofitClient.api.getWeather(city.name, apiKey)
                .enqueue(object : Callback<OwmResponse> {
                    override fun onResponse(
                        call: Call<OwmResponse>,
                        response: Response<OwmResponse>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let { body ->
                                results.add(
                                    CityWeather(
                                        cityName = body.name,
                                        temp = body.main.temp,
                                        description = body.weather.firstOrNull()?.description.orEmpty(),
                                        humidity = body.main.humidity,
                                        windSpeed = body.wind.speed
                                    )
                                )
                                Log.d(TAG, "${body.name}: ${body.main.temp}°C")
                            }
                        } else {
                            Log.e(TAG, "${city.name}: HTTP ${response.code()}")
                            Toast.makeText(
                                this@CityWeatherActivity,
                                "${city.name}: HTTP ${response.code()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        onOneRequestFinished()
                    }

                    override fun onFailure(call: Call<OwmResponse>, t: Throwable) {
                        Log.e(TAG, "${city.name}: ${t.message}")
                        Toast.makeText(
                            this@CityWeatherActivity,
                            getString(R.string.lab14_city_error, city.name, t.message ?: ""),
                            Toast.LENGTH_SHORT
                        ).show()
                        onOneRequestFinished()
                    }
                })
        }
    }

    private fun onOneRequestFinished() {
        pendingRequests--
        if (pendingRequests <= 0) {
            // можно отсортировать по имени
            adapter.submitList(results.sortedBy { it.cityName })
            Toast.makeText(
                this,
                getString(R.string.lab14_done, results.size),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            // частичное обновление по мере ответов
            adapter.submitList(results.sortedBy { it.cityName })
        }
    }

    override fun onDestroy() {
        db.close()
        super.onDestroy()
    }
}