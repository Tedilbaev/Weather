package com.example.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CityAdapter(
    private val cities: List<City>
) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCityName: TextView = itemView.findViewById(R.id.tvCityName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_city, parent, false)
        return CityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = cities[position]
        holder.tvCityName.text = city.name
    }

    override fun getItemCount(): Int = cities.size
}