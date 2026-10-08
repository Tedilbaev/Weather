package com.example.weather.lab8

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.weather.R

class CityListActivity : AppCompatActivity(), CityListFragment.OnCitySelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_list)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, CityListFragment())
                .commit()
        }
    }

    override fun onCitySelected(city: City) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, CityDetailFragment.newInstance(city.name))
            .addToBackStack(null)
            .commit()
    }

    fun returnSelectedCity(cityName: String) {
        val resultIntent = Intent()
        resultIntent.putExtra(LoginActivity.EXTRA_SELECTED_CITY, cityName)
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}