package com.example.weatherapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.ui.CurrentWeather
import com.example.weatherapp.domain.utils.LocationManager
import com.example.weatherapp.ui.viewModel.LocationViewModel
import androidx.activity.SystemBarStyle

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val locationViewModel: LocationViewModel by viewModels()
    private lateinit var locationManager: LocationManager

    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.INTERNET
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.dark(0)
        )


        locationManager = LocationManager(this)

        if (hasAllPermissions()) {
            if (locationManager.hasGpsOrNetwork()) {
                locationViewModel.fetchLocation()
            } else {
                locationManager.promptEnableGps()
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, 100)
        }

        if (savedInstanceState == null) {
            loadFragment(CurrentWeather(), addToBackStack = false)
        }
    }

    private fun hasAllPermissions(): Boolean {
        return permissions.all {
            ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 100 && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            if (locationManager.hasGpsOrNetwork()) {
                locationViewModel.fetchLocation()
            } else {
                locationManager.promptEnableGps()
            }
        } else {
            Toast.makeText(
                this,
                "Location permission is required to use this feature.",
                Toast.LENGTH_LONG
            ).show()
        }

    }


    fun loadFragment(fragment: Fragment, addToBackStack: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }


}
