package com.example.weatherapp.domain.models.weather

data class CurrentWeather(
    val description: String,
    val tempmax : Double,
    val tempmin : Double,
    val temperature: Double,
    val feelsLike: Double,
    val humidity: Double,
    val windSpeed: Double,
    val pressure: Double,
    val visibility: Double,
    val cloudCover: Double,
    val uvIndex: Int,
    val conditions: String,
    val icon: String,
    val sunrise: String,
    val sunset: String,
    val latitude: Double,
    val longitude: Double
)

data class ForecastWeather(
    val forecasts: List<DailyForecast>,
    val latitude: Double,
    val longitude: Double
)

data class DailyForecast(
    val date: String,
    val tempMax: Double,
    val tempMin: Double,
    val tempAvg: Double,
    val humidity: Double,
    val windSpeed: Double,
    val pressure: Double,
    val uvIndex: Int,
    val description: String,
    val icon: String
)