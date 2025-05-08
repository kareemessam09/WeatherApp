package com.example.weatherapp.ui.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.domain.repositories.LocationNameRepo
import com.example.weatherapp.domain.utils.LocationManager

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val locationManager = LocationManager(application)
    private val _locationLiveData = MutableLiveData<Pair<String, String>>()
    val locationLiveData: LiveData<Pair<String, String>> get() = _locationLiveData


    private val _error = MutableLiveData<String>("Unknown Location")
    val error: LiveData<String> = _error

    fun fetchLocation() {
        locationManager.getCurrentLocation(
            onSuccess = { lat, lon ->
                Log.d("LocationViewModel", "Fetched location: $lat, $lon")
                _locationLiveData.postValue(Pair(lat, lon))
            },
            onFailure = { e ->
                _error.postValue(e.message ?: "Unknown error")
            }
        )
    }

}
