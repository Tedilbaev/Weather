package com.example.weather

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class CityListFragment : Fragment(R.layout.fragment_city_list) {

    private lateinit var adapter: CityAdapter
    private var listener: OnCitySelectedListener? = null

    companion object {
        private const val TAG = "CityListFragment"
    }

    interface OnCitySelectedListener {
        fun onCitySelected(city: City)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach")
        listener = context as? OnCitySelectedListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")

        val cities = mutableListOf(
            City("Москва"), City("Санкт-Петербург"), City("Новосибирск"),
            City("Екатеринбург"), City("Казань"), City("Нижний Новгород"),
            City("Челябинск"), City("Самара"), City("Омск"), City("Ростов-на-Дону")
        )

        adapter = CityAdapter(cities) { city ->
            listener?.onCitySelected(city)
        }

        view.findViewById<RecyclerView>(R.id.rvCities).adapter = adapter
    }

    override fun onStart() { super.onStart(); Log.d(TAG, "onStart") }
    override fun onResume() { super.onResume(); Log.d(TAG, "onResume") }
    override fun onPause() { super.onPause(); Log.d(TAG, "onPause") }
    override fun onStop() { super.onStop(); Log.d(TAG, "onStop") }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView")
    }

    override fun onDestroy() { super.onDestroy(); Log.d(TAG, "onDestroy") }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach")
        listener = null
    }
}