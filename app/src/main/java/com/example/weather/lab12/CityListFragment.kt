package com.example.weather.lab12

import android.content.Context
import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.lab11.City
import com.example.weather.lab11.WeatherDbHelper

class CityListFragment : Fragment(R.layout.lab8_fragment_city_list) {

    private lateinit var db: WeatherDbHelper
    private lateinit var adapter: CityAdapter
    private var selectedCity: City? = null
    private var listener: OnCitySelectedListener? = null

    interface OnCitySelectedListener {
        fun onCitySelected(city: City)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnCitySelectedListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = WeatherDbHelper(requireContext())

        adapter = CityAdapter(
            cities = mutableListOf(),
            onCityClick = { city -> listener?.onCitySelected(city) },
            onRegisterContextMenu = { itemView -> registerForContextMenu(itemView) }
        )
        view.findViewById<RecyclerView>(R.id.rvCities).adapter = adapter

        view.findViewById<Button>(R.id.btnAddCity).setOnClickListener { showAddDialog() }

        seedIfEmpty()
        reload()
    }

    private fun seedIfEmpty() {
        if (db.getAllCities().isEmpty()) {
            db.addCity("Москва", "Столица, переменная облачность")
            db.addCity("Санкт-Петербург", "Дожди, +8°")
            db.addCity("Казань", "Ясно, +12°")
        }
    }

    private fun reload() {
        adapter.submitList(db.getAllCities())
    }

    private fun showAddDialog() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.lab12_dialog_city, null)
        val etName = dialogView.findViewById<EditText>(R.id.etCityName)
        val etDesc = dialogView.findViewById<EditText>(R.id.etCityDescription)

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_add_city_title)
            .setView(dialogView)
            .setPositiveButton(R.string.btn_add) { _, _ ->
                val name = etName.text.toString().trim()
                val desc = etDesc.text.toString().trim()
                if (name.isNotBlank()) {
                    db.addCity(name, desc)
                    reload()
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun showEditDialog(city: City) {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.lab12_dialog_city, null)
        val etName = dialogView.findViewById<EditText>(R.id.etCityName)
        val etDesc = dialogView.findViewById<EditText>(R.id.etCityDescription)
        etName.setText(city.name)
        etDesc.setText(city.description)

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.lab12_edit_title)
            .setView(dialogView)
            .setPositiveButton(R.string.lab12_save) { _, _ ->
                val name = etName.text.toString().trim()
                val desc = etDesc.text.toString().trim()
                if (name.isNotBlank()) {
                    db.updateCity(city.id, name, desc)
                    reload()
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val city = v.tag as? City ?: return
        selectedCity = city
        menu.setHeaderTitle(city.name)
        requireActivity().menuInflater.inflate(R.menu.menu_context_city, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val city = selectedCity ?: return super.onContextItemSelected(item)
        return when (item.itemId) {
            R.id.action_city_details -> {
                showEditDialog(city)
                true
            }
            R.id.action_city_delete -> {
                AlertDialog.Builder(requireContext())
                    .setTitle(R.string.dialog_delete_title)
                    .setMessage(getString(R.string.dialog_delete_message, city.name))
                    .setPositiveButton(R.string.yes) { _, _ ->
                        db.deleteCity(city.id)
                        reload()
                    }
                    .setNegativeButton(R.string.no, null)
                    .show()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onDestroyView() {
        db.close()
        super.onDestroyView()
    }
}