package com.example.weatherapp.data.online

import android.util.Log
import com.example.weatherapp.domain.models.weather.CurrentWeather
import com.example.weatherapp.domain.models.weather.DailyForecast
import com.example.weatherapp.domain.models.weather.ForecastWeather
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.min

class WeatherApi : API {

    companion object {
        private const val BASE_URL = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline"
        private const val API_KEY = "8PLZWU5Z5F2R74S6B3733NRKC"
        private const val CONNECT_TIMEOUT = 15000 // 15 seconds
        private const val READ_TIMEOUT = 15000 // 15 seconds
    }

    override fun fetchCurrentWeather(lat: Double, lon: Double): CurrentWeather? {
        var connection: HttpURLConnection? = null
        return try {
            val url = URL("$BASE_URL/$lat,$lon/today?unitGroup=metric&key=$API_KEY&contentType=json")

            connection = url.openConnection() as HttpURLConnection
            connection.apply {
                requestMethod = "GET"
                connectTimeout = CONNECT_TIMEOUT
                readTimeout = READ_TIMEOUT
                doInput = true
                useCaches = false
            }

            when (connection.responseCode) {
                HttpURLConnection.HTTP_OK -> {
                    connection.inputStream.bufferedReader().use { reader ->
                        parseCurrentWeather(reader.readText())
                    }
                }
                else -> {
                    Log.e("WeatherAPI", "HTTP Error: ${connection.responseCode}")
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("WeatherAPI", "Network error", e)
            null
        } finally {
            connection?.disconnect()
        }
    }


    private fun parseCurrentWeather(json: String): CurrentWeather {
        val root = JSONObject(json)
        val current = root.getJSONObject("currentConditions")
        val day = root.getJSONArray("days").getJSONObject(0)

        println("Icon : ${current.optString("icon", "")}")

        return CurrentWeather(
            tempmax = day.optDouble("tempmax", Double.NaN),
            tempmin = day.optDouble("tempmin", Double.NaN),
            description = root.optString("description", "Unknown"),
            temperature = current.optDouble("temp", Double.NaN),
            feelsLike = current.optDouble("feelslike", Double.NaN),
            humidity = current.optDouble("humidity", Double.NaN),
            windSpeed = current.optDouble("windspeed", Double.NaN),
            pressure = current.optDouble("pressure", Double.NaN),
            visibility = current.optDouble("visibility", Double.NaN),
            cloudCover = current.optDouble("cloudcover", Double.NaN),
            uvIndex = current.optInt("uvindex", -1),
            conditions = current.optString("conditions", "Unknown"),
            icon = current.optString("icon", ""),
            sunrise = day.optString("sunrise", ""),
            sunset = day.optString("sunset", ""),
            latitude = root.optDouble("latitude", Double.NaN),
            longitude = root.optDouble("longitude", Double.NaN)
        )
    }

    override fun fetchFiveDayForecast(lat: Double, lon: Double): ForecastWeather? {
        var connection: HttpURLConnection? = null
        return try {
            val url = URL("$BASE_URL/$lat,$lon?unitGroup=metric&key=$API_KEY&contentType=json&include=days")


            connection = url.openConnection() as HttpURLConnection
            connection.apply {
                requestMethod = "GET"
                connectTimeout = CONNECT_TIMEOUT
                readTimeout = READ_TIMEOUT
                doInput = true
                useCaches = false
            }

            when (connection.responseCode) {
                HttpURLConnection.HTTP_OK -> {
                    connection.inputStream.bufferedReader().use { reader ->
                        parseFiveDayForecast(reader.readText())
                    }
                }
                else -> {
                    Log.e("WeatherAPI", "HTTP Error: ${connection.responseCode}")
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("WeatherAPI", "Network error", e)
            null
        } finally {
            connection?.disconnect()
        }
    }




    private fun parseFiveDayForecast(json: String): ForecastWeather {
        val root = JSONObject(json)
        val days = root.getJSONArray("days")
        val forecasts = mutableListOf<DailyForecast>()

        val dayCount = min(5, days.length())
        for (i in 0 until dayCount) {
            val day = days.getJSONObject(i)
            forecasts.add(
                DailyForecast(
                    date = day.getString("datetime"),
                    tempMax = day.getDouble("tempmax"),
                    tempMin = day.getDouble("tempmin"),
                    icon = day.getString("icon"),
                    humidity = day.getDouble("humidity"),
                    windSpeed = day.getDouble("windspeed"),
                    tempAvg = day.getDouble("temp"),
                    pressure = day.getDouble("pressure"),
                    uvIndex = day.getInt("uvindex"),
                    description = day.getString("conditions"),
                )
            )
        }
        return ForecastWeather(
            forecasts = forecasts,
            latitude = root.getDouble("latitude"),
            longitude = root.getDouble("longitude")
        )
    }
}
