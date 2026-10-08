package com.example.weather.lab6

import com.example.weather.R
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class CityListActivity : AppCompatActivity() {

    private lateinit var adapter: CityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lab5_activity_city_list)

        val login = intent.getStringExtra(LoginActivity.EXTRA_LOGIN)
        if (login != null) {
            Toast.makeText(this, "Пользователь: $login", Toast.LENGTH_SHORT).show()
        }

        val cities = listOf(
            City("Москва"),
            City("Санкт-Петербург"),
            City("Новосибирск"),
            City("Екатеринбург"),
            City("Казань"),
            City("Нижний Новгород"),
            City("Челябинск"),
            City("Самара"),
            City("Омск"),
            City("Ростов-на-Дону"),
            City("Уфа"),
            City("Красноярск"),
            City("Воронеж"),
            City("Пермь"),
            City("Волгоград"),
            City("Краснодар"),
            City("Саратов"),
            City("Тюмень"),
            City("Тольятти"),
            City("Ижевск"),
            City("Барнаул"),
            City("Ульяновск"),
            City("Иркутск"),
            City("Хабаровск"),
            City("Ярославль"),
            City("Владивосток"),
            City("Махачкала"),
            City("Томск"),
            City("Оренбург"),
            City("Кемерово"),
            City("Новокузнецк"),
            City("Рязань"),
            City("Астрахань")
        )

        adapter = CityAdapter(cities) { selectedCity ->
            returnSelectedCity(selectedCity.name)
        }
        findViewById<RecyclerView>(R.id.rvCities).adapter = adapter
    }

    private fun returnSelectedCity(cityName: String) {
        val resultIntent = Intent()
        resultIntent.putExtra(LoginActivity.EXTRA_SELECTED_CITY, cityName)
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}