package com.example.weatherapp.domain.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.location.CurrentLocationRequest

class LocationManager(private val activity: Context) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
    private val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager


    fun hasGpsOrNetwork(): Boolean {
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return isGpsEnabled || isNetworkEnabled
    }

    fun promptEnableGps() {
        if (activity is Activity) {
            AlertDialog.Builder(activity)
                .setTitle("GPS Disabled")
                .setMessage("GPS is required for this app to work properly. Would you like to enable it?")
                .setPositiveButton("Yes") { _, _ ->
                    activity.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                    Toast.makeText(
                        activity,
                        "GPS is required for weather data based on your location",
                        Toast.LENGTH_LONG
                    ).show()
                }
                .setCancelable(false)
                .show()
        }
    }

    fun getCurrentLocation(
        onSuccess: (latitude: String, longitude: String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            try {
                val locationRequest = CurrentLocationRequest.Builder()
                    .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                    .build()

                fusedLocationClient
                    .getCurrentLocation(locationRequest, null)
                    .addOnSuccessListener { location ->
                        if (location != null) {
                            val latitude = location.latitude
                            val longitude = location.longitude
                            onSuccess(latitude.toString(), longitude.toString())
                        } else {
                            onFailure(Exception("Location is null - GPS might be disabled"))
                        }
                    }
                    .addOnFailureListener { exception ->
                        onFailure(exception)
                    }
            } catch (e: Exception) {
                onFailure(e)
            }
        } else {
            onFailure(Exception("Location permission not granted"))
        }
    }
}
