package com.example.weather.lab12

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.lab11.City

class CityAdapter(
    private val cities: MutableList<City>,
    private val onCityClick: (City) -> Unit,
    private val onRegisterContextMenu: (View) -> Unit
) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCityName: TextView = itemView.findViewById(R.id.tvCityName)
        val tvDescription: TextView? = itemView.findViewById(R.id.tvDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.lab12_item_city, parent, false)
        return CityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = cities[position]
        holder.tvCityName.text = city.name
        holder.tvDescription?.text = city.description
        holder.itemView.tag = city
        holder.itemView.setOnClickListener { onCityClick(city) }
        onRegisterContextMenu(holder.itemView)
    }

    override fun getItemCount(): Int = cities.size

    fun submitList(newList: List<City>) {
        cities.clear()
        cities.addAll(newList)
        notifyDataSetChanged()
    }
}