package com.example.weatherapp.data.online

import com.example.weatherapp.domain.models.weather.CurrentWeather
import com.example.weatherapp.domain.models.weather.ForecastWeather

interface API {
    fun fetchCurrentWeather(lat: Double, lon: Double): CurrentWeather?
    fun fetchFiveDayForecast(lat: Double, lon: Double): ForecastWeather?
}