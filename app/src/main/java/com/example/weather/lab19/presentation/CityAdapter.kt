package com.example.weather.lab19.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.lab19.data.City

class CityAdapter(
    private val onCityClick: (City) -> Unit,
    private val onCityLongClick: (City) -> Unit
) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {
    private var cities: List<City> = emptyList()

    class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvCityName)
        val description: TextView = itemView.findViewById(R.id.tvDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder =
        CityViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.lab19_item_city, parent, false))

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = cities[position]
        holder.name.text = city.name
        holder.description.text = city.description
        holder.itemView.setOnClickListener { onCityClick(city) }
        holder.itemView.setOnLongClickListener {
            onCityLongClick(city)
            true
        }
    }

    override fun getItemCount(): Int = cities.size

    fun submitList(newCities: List<City>) {
        cities = newCities
        notifyDataSetChanged()
    }
}
