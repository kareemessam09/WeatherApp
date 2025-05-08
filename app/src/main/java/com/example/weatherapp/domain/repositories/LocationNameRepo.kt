package com.example.weatherapp.domain.repositories

import com.example.weatherapp.data.online.ReverseGeocodingService

class LocationNameRepo (
    private val reverseGeocodingService: ReverseGeocodingService = ReverseGeocodingService()
){
    fun getLocationName(lat: Double, lon: Double): String {
        return reverseGeocodingService.getAddressFromCoordinates(lat, lon)
    }
}