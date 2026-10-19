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
        private const val API_KEY = "3bd4af208d5783c29a4366e4ad1956a1"
        private const val CITY = "Moscow"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lab13_activity_network)

        findViewById<Button>(R.id.btnLoadWeather).setOnClickListener {
            loadWeather()
        }
    }

    private fun loadWeather() {
        Log.d(TAG, "Запрос погоды: city=$CITY")

        RetrofitClient.api.getWeather(CITY, API_KEY)
            .enqueue(object : Callback<OwmResponse> {
                override fun onResponse(
                    call: Call<OwmResponse>,
                    response: Response<OwmResponse>
                ) {
                    if (!response.isSuccessful) {
                        Log.e(TAG, "HTTP ${response.code()} ${response.message()}")
                        Toast.makeText(
                            this@NetworkActivity,
                            "HTTP ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }

                    val body = response.body()
                    if (body == null) {
                        Log.w(TAG, "Пустой body")
                        return
                    }

                    val desc = body.weather.firstOrNull()?.description.orEmpty()

                    Log.d(TAG, "----- Погода -----")
                    Log.d(TAG, "Город: ${body.name}")
                    Log.d(TAG, "Температура: ${body.main.temp} °C")
                    Log.d(TAG, "Влажность: ${body.main.humidity}%")
                    Log.d(TAG, "Давление: ${body.main.pressure} гПа")
                    Log.d(TAG, "Ветер: ${body.wind.speed} м/с")
                    Log.d(TAG, "Описание: $desc")
                    Log.d(TAG, "------------------")

                    Toast.makeText(
                        this@NetworkActivity,
                        R.string.lab13_success,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onFailure(call: Call<OwmResponse>, t: Throwable) {
                    Log.e(TAG, "Ошибка сети: ${t.message}", t)
                    Toast.makeText(
                        this@NetworkActivity,
                        R.string.lab13_error,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}