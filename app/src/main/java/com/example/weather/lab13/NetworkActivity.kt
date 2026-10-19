package com.example.weather.lab13

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.weather.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "Lab13_Weather"
        private const val LAT = 55.75  // Москва
        private const val LON = 37.62
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lab13_activity_network)

        findViewById<Button>(R.id.btnLoadWeather).setOnClickListener {
            loadWeather()
        }
    }

    private fun loadWeather() {
        Log.d(TAG, "Запрос погоды: lat=$LAT, lon=$LON")

        RetrofitClient.api.getCurrentWeather(LAT, LON)
            .enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    if (!response.isSuccessful) {
                        Log.e(TAG, "HTTP ${response.code()}")
                        Toast.makeText(this@NetworkActivity, "HTTP ${response.code()}", Toast.LENGTH_SHORT).show()
                        return
                    }

                    val current = response.body()?.currentWeather
                    if (current == null) {
                        Log.w(TAG, "current_weather пустой")
                        return
                    }

                    // главное для ЛР13 — вывод в Log
                    Log.d(TAG, "----- Погода (Москва) -----")
                    Log.d(TAG, "Температура: ${current.temperature} °C")
                    Log.d(TAG, "Ветер: ${current.windSpeed} км/ч")
                    Log.d(TAG, "Направление: ${current.windDirection}°")
                    Log.d(TAG, "Код погоды: ${current.weatherCode}")
                    Log.d(TAG, "Время: ${current.time}")
                    Log.d(TAG, "---------------------------")

                    Toast.makeText(this@NetworkActivity, R.string.lab13_success, Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    Log.e(TAG, "Ошибка сети: ${t.message}", t)
                    Toast.makeText(this@NetworkActivity, R.string.lab13_error, Toast.LENGTH_SHORT).show()
                }
            })
    }
}