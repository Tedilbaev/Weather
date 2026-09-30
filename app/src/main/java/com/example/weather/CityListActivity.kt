package com.example.weather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class CityListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_list)

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

        val recyclerView = findViewById<RecyclerView>(R.id.rvCities)
        recyclerView.adapter = CityAdapter(cities)
    }
}