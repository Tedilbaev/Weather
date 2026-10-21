package com.example.weather.lab19.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.lab19.data.City
import com.example.weather.lab19.viewmodel.CityListViewModel

class CityListActivity : AppCompatActivity() {
    private val viewModel: CityListViewModel by viewModels()
    private lateinit var adapter: CityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lab19_activity_city_list)
        title = getString(R.string.lab19_title)

        adapter = CityAdapter(
            onCityClick = ::showEditDialog,
            onCityLongClick = ::showDeleteDialog
        )
        findViewById<RecyclerView>(R.id.rvLab19Cities).apply {
            layoutManager = LinearLayoutManager(this@CityListActivity)
            adapter = this@CityListActivity.adapter
        }
        findViewById<Button>(R.id.btnLab19AddCity).setOnClickListener {
            showCityDialog(null)
        }

        viewModel.cities.observe(this) { cities -> adapter.submitList(cities) }
    }

    private fun showEditDialog(city: City) = showCityDialog(city)

    private fun showCityDialog(city: City?) {
        val dialogView = LayoutInflater.from(this)
            .inflate(R.layout.lab19_dialog_city, null)
        val name = dialogView.findViewById<EditText>(R.id.etLab19CityName)
        val description = dialogView.findViewById<EditText>(R.id.etLab19CityDescription)
        name.setText(city?.name.orEmpty())
        description.setText(city?.description.orEmpty())

        AlertDialog.Builder(this)
            .setTitle(if (city == null) R.string.lab19_add_title else R.string.lab19_edit_title)
            .setView(dialogView)
            .setPositiveButton(if (city == null) R.string.btn_add else R.string.lab19_save) { _, _ ->
                val cityName = name.text.toString().trim()
                val cityDescription = description.text.toString().trim()
                if (city == null) viewModel.addCity(cityName, cityDescription)
                else viewModel.updateCity(city, cityName, cityDescription)
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun showDeleteDialog(city: City) {
        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_delete_title)
            .setMessage(getString(R.string.dialog_delete_message, city.name))
            .setPositiveButton(R.string.yes) { _, _ -> viewModel.deleteCity(city) }
            .setNegativeButton(R.string.no, null)
            .show()
    }
}
