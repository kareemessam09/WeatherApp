package com.example.weatherapp.data.online


import android.util.Log
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class ReverseGeocodingService {

    companion object {
        private const val API_KEY = "6200c614b91946a988812477e2cc7f78"
        private const val BASE_URL = "https://api.opencagedata.com/geocode/v1/json"
        private const val TIMEOUT = 15000 // 15 seconds
    }

    fun getAddressFromCoordinates(lat: Double, lon: Double): String {
        val requestUrl = "$BASE_URL?q=$lat+$lon&key=$API_KEY"
        var connection: HttpURLConnection? = null

        return try {
            val url = URL(requestUrl)
            connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = TIMEOUT
            connection.readTimeout = TIMEOUT
            connection.setRequestProperty("User-Agent", "WeatherApp")

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().readText()
                val json = JSONObject(response)
                val results = json.getJSONArray("results")
                if (results.length() > 0) {
                    val firstResult = results.getJSONObject(0)
                    firstResult.getString("formatted")
                } else {
                    "Unknown Location"
                }
            } else {
                Log.e("ReverseGeocoding", "HTTP Error: ${connection.responseCode}")
                "Unknown Location"
            }
        } catch (e: Exception) {
            Log.e("ReverseGeocoding", "Network error", e)
            "Unknown Location"
        } finally {
            connection?.disconnect()
        }
    }
}
