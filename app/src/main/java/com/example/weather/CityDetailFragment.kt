package com.example.weather

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class CityDetailFragment : Fragment(R.layout.fragment_city_detail) {

    companion object {
        private const val TAG = "CityDetailFragment"
        private const val ARG_CITY_NAME = "arg_city_name"

        fun newInstance(cityName: String): CityDetailFragment {
            val fragment = CityDetailFragment()
            val args = Bundle()
            args.putString(ARG_CITY_NAME, cityName)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")

        val cityName = arguments?.getString(ARG_CITY_NAME) ?: ""
        view.findViewById<TextView>(R.id.tvCityDetailName).text = cityName

        view.findViewById<Button>(R.id.btnConfirmCity).setOnClickListener {
            (activity as? CityListActivity)?.returnSelectedCity(cityName)
        }
    }

    override fun onStart() { super.onStart(); Log.d(TAG, "onStart") }
    override fun onResume() { super.onResume(); Log.d(TAG, "onResume") }
    override fun onPause() { super.onPause(); Log.d(TAG, "onPause") }
    override fun onStop() { super.onStop(); Log.d(TAG, "onStop") }
    override fun onDestroyView() { super.onDestroyView(); Log.d(TAG, "onDestroyView") }
    override fun onDestroy() { super.onDestroy(); Log.d(TAG, "onDestroy") }
}