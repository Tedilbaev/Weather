package com.example.weather.lab12

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.weather.R

class CityDetailFragment : Fragment(R.layout.lab8_fragment_city_detail) {

    companion object {
        private const val ARG_NAME = "arg_name"
        private const val ARG_DESC = "arg_desc"

        fun newInstance(name: String, description: String): CityDetailFragment {
            return CityDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_NAME, name)
                    putString(ARG_DESC, description)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = arguments?.getString(ARG_NAME).orEmpty()
        val desc = arguments?.getString(ARG_DESC).orEmpty()

        view.findViewById<TextView>(R.id.tvCityDetailName).text = name
        view.findViewById<TextView>(R.id.tvCityDetailDesc)?.text = desc

        view.findViewById<Button>(R.id.btnConfirmCity).setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}