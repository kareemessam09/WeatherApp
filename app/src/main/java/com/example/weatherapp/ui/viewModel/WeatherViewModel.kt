package com.example.weatherapp.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.domain.models.weather.CurrentWeather
import com.example.weatherapp.domain.repositories.WeatherRepository
import com.example.weatherapp.domain.models.weather.ForecastWeather
import com.example.weatherapp.domain.repositories.LocationNameRepo
import java.lang.Exception

class WeatherViewModel() : ViewModel() {

    private val repository: WeatherRepository = WeatherRepository()

    private var _currentWeather = MutableLiveData<CurrentWeather?>()
    val currentWeather: LiveData<CurrentWeather?> = _currentWeather

    private var _fiveDayForecast = MutableLiveData<ForecastWeather?>()
    val fiveDayForecast: LiveData<ForecastWeather?> = _fiveDayForecast

    private var _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _isLoadingCurrent = MutableLiveData<Boolean>(false)
    val isLoadingCurrent: LiveData<Boolean> = _isLoadingCurrent

    private val _isLoadingForecast = MutableLiveData<Boolean>(false)
    val isLoadingForecast: LiveData<Boolean> = _isLoadingForecast

    var cachedWeather: CurrentWeather? = null
    var cachedForecast: ForecastWeather? = null

    private val locationNameRepo = LocationNameRepo()


    val _locationName = MutableLiveData<String>()
    val locationName: LiveData<String> = _locationName




    fun loadCurrentWeather(lat: Double, lon: Double) {

        if (cachedWeather != null) {
            _currentWeather.postValue(cachedWeather)
            _isLoadingCurrent.postValue(false)
            return
        }

        _isLoadingCurrent.postValue(true)
        Thread {
            try {
                val current = repository.getCurrentWeather(lat, lon)
                _locationName.postValue(locationNameRepo.getLocationName(lat, lon))
                println("Location name: ${_locationName.value}")
                cachedWeather = current
                _currentWeather.postValue(current)
                println("Current weather loaded: $current")
            } catch (e: Exception) {
                _error.postValue("Failed to load current weather: ${e.message}")
            } finally {
                _isLoadingCurrent.postValue(false)
            }
        }.start()
    }

    fun loadFiveDayForecast(lat: Double, lon: Double) {
        if (cachedForecast != null) {
            println("Returning cached forecast")
            _fiveDayForecast.postValue(cachedForecast)
            _isLoadingForecast.postValue(false)
            return
        }

        _isLoadingForecast.postValue(true)
        Thread {
            try {
                val forecast = repository.getFiveDayForecast(lat, lon)
                cachedForecast = forecast
                println("saved cached forecast $cachedForecast")
                _fiveDayForecast.postValue(forecast)
            } catch (e: Exception) {
                _error.postValue("Failed to load forecast: ${e.message}")
            } finally {
                _isLoadingForecast.postValue(false)
            }
        }.start()


    }

    fun extractRegion(fullAddress: String , cut : Int): String {
        val parts = fullAddress.split(",").map { it.trim() }
        return if (parts.size > 3) parts[parts.size - cut] else "Unknown"
    }





}