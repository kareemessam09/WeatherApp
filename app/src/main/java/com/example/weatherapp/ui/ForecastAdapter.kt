package com.example.weatherapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.domain.models.weather.DailyForecast
import kotlin.text.clear

class ForecastAdapter(
    private val forecasts: List<DailyForecast>
) : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    inner class ForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.icon)
        val date: TextView = itemView.findViewById(R.id.day)
        val description: TextView = itemView.findViewById(R.id.condition)
        val temp: TextView = itemView.findViewById(R.id.temp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.for_cast_item, parent, false)
        return ForecastViewHolder(view)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val forecast = forecasts[position]
        holder.date.text = forecast.date
        holder.description.text = forecast.description
        holder.temp.text = "${forecast.tempMax.toInt()}°C" + " / ${forecast.tempMin.toInt()}°C"

        val iconRes = when (forecast.description) {
            "Clear" -> R.drawable.clear
            "Partially cloudy" -> R.drawable.cloudy
            "Rain" -> R.drawable.rainy
            else -> R.drawable.sun_rainy
        }
        holder.icon.setImageResource(iconRes)
    }

    override fun getItemCount(): Int = forecasts.size
}
