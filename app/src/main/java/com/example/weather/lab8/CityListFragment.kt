package com.example.weather.lab8

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

class CityListFragment : Fragment(R.layout.fragment_city_list) {

    private lateinit var adapter: CityAdapter
    private val cities = mutableListOf(
        City("Москва"), City("Санкт-Петербург"), City("Новосибирск"),
        City("Екатеринбург"), City("Казань"), City("Нижний Новгород"),
        City("Челябинск"), City("Самара"), City("Омск"), City("Ростов-на-Дону")
    )
    private var selectedCity: City? = null
    private var listener: OnCitySelectedListener? = null

    companion object {
        private const val TAG = "CityListFragment"
    }

    interface OnCitySelectedListener {
        fun onCitySelected(city: City)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnCitySelectedListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CityAdapter(
            cities = cities,
            onCityClick = { city -> listener?.onCitySelected(city) },
            onRegisterContextMenu = { itemView -> registerForContextMenu(itemView) }
        )

        view.findViewById<RecyclerView>(R.id.rvCities).adapter = adapter

        view.findViewById<Button>(R.id.btnAddCity).setOnClickListener {
            showAddCityDialog()
        }
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
                listener?.onCitySelected(city)
                true
            }
            R.id.action_city_delete -> {
                showDeleteConfirmation(city)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun showDeleteConfirmation(city: City) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_delete_title)
            .setMessage(getString(R.string.dialog_delete_message, city.name))
            .setPositiveButton(R.string.yes) { _, _ ->
                val position = cities.indexOf(city)
                if (position != -1) adapter.removeCity(position)
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    private fun showAddCityDialog() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_add_city, null)
        val etName = dialogView.findViewById<EditText>(R.id.etNewCityName)

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_add_city_title)
            .setView(dialogView)
            .setPositiveButton(R.string.btn_add) { _, _ ->
                val name = etName.text.toString().trim()
                if (name.isNotBlank()) {
                    adapter.addCity(City(name))
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}