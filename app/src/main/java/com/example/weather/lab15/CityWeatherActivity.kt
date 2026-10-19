package com.example.weather.lab15

import com.example.weather.BuildConfig
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.lab11.WeatherDbHelper
import com.example.weather.lab14.CityWeather
import com.example.weather.lab14.CityWeatherAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CityWeatherActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "Lab15_Weather"
        private val apiKey: String
            get() = BuildConfig.OWM_API_KEY
    }

    private lateinit var db: WeatherDbHelper
    private lateinit var adapter: CityWeatherAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var btnLoad: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lab15_activity_city_weather)

        db = WeatherDbHelper(this)
        adapter = CityWeatherAdapter()
        progressBar = findViewById(R.id.progressBar)
        btnLoad = findViewById(R.id.btnLoad)

        findViewById<RecyclerView>(R.id.rvCities).adapter = adapter

        seedCitiesIfNeeded()

        btnLoad.setOnClickListener {
            loadWeather()
        }
    }

    private fun seedCitiesIfNeeded() {
        if (db.getAllCities().isEmpty()) {
            db.addCity("Moscow", "Москва")
            db.addCity("Saint Petersburg", "Санкт-Петербург")
            db.addCity("Kazan", "Казань")
        }
    }

    private fun loadWeather() {
        val cities = db.getAllCities()
        if (cities.isEmpty()) {
            Toast.makeText(this, R.string.lab14_no_cities, Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            setLoading(true)
            try {
                val results = withContext(Dispatchers.IO) {
                    // параллельно в фоне
                    cities.map { city ->
                        async {
                            try {
                                val body = RetrofitClient.api.getWeather(city.name, apiKey)
                                CityWeather(
                                    cityName = body.name,
                                    temp = body.main.temp,
                                    description = body.weather.firstOrNull()?.description.orEmpty(),
                                    humidity = body.main.humidity,
                                    windSpeed = body.wind.speed
                                )
                            } catch (e: Exception) {
                                Log.e(TAG, "${city.name}: ${e.message}")
                                null
                            }
                        }
                    }.awaitAll().filterNotNull()
                }

                adapter.submitList(results.sortedBy { it.cityName })
                Toast.makeText(
                    this@CityWeatherActivity,
                    getString(R.string.lab14_done, results.size),
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка", e)
                Toast.makeText(
                    this@CityWeatherActivity,
                    getString(R.string.lab14_network_error, e.message ?: ""),
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                setLoading(false)
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        btnLoad.isEnabled = !loading
    }

    override fun onDestroy() {
        db.close()
        super.onDestroy()
    }
}