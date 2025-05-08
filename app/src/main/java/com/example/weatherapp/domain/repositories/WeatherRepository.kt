package com.example.weatherapp.domain.repositories

import com.example.weatherapp.data.online.API
import com.example.weatherapp.data.online.WeatherApi
import com.example.weatherapp.domain.models.weather.CurrentWeather
import com.example.weatherapp.domain.models.weather.ForecastWeather

class WeatherRepository(private val api: API = WeatherApi()) {

    fun getCurrentWeather(lat: Double, lon: Double): CurrentWeather? {
        return api.fetchCurrentWeather(lat, lon)
    }

    fun getFiveDayForecast(lat: Double, lon: Double): ForecastWeather? {
        return api.fetchFiveDayForecast(lat, lon)
    }
}
