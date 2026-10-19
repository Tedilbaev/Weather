package com.example.weather.lab14

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R

class CityWeatherAdapter(
    private val items: MutableList<CityWeather> = mutableListOf()
) : RecyclerView.Adapter<CityWeatherAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvCity: TextView = view.findViewById(R.id.tvCity)
        val tvTemp: TextView = view.findViewById(R.id.tvTemp)
        val tvDesc: TextView = view.findViewById(R.id.tvDesc)
        val tvExtra: TextView = view.findViewById(R.id.tvExtra)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.lab14_item_city_weather, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.tvCity.text = item.cityName
        holder.tvTemp.text = "${item.temp} °C"
        holder.tvDesc.text = item.description
        holder.tvExtra.text = "Влажность: ${item.humidity}%  ·  Ветер: ${item.windSpeed} м/с"
    }

    override fun getItemCount() = items.size

    fun submitList(list: List<CityWeather>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}