package com.example.weather.lab12

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.weather.R
import com.example.weather.lab11.City

class CityListActivity : AppCompatActivity(), CityListFragment.OnCitySelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lab8_activity_city_list)

        // логин с экрана входа (по желанию)
        // val login = intent.getStringExtra(LoginActivity.EXTRA_LOGIN)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, CityListFragment())
                .commit()
        }
    }

    override fun onCitySelected(city: City) {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer,
                CityDetailFragment.newInstance(city.name, city.description)
            )
            .addToBackStack(null)
            .commit()
    }
}