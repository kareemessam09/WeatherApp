package com.example.weatherapp.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.weatherapp.MainActivity
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentCurrentWeatherBinding
import com.example.weatherapp.domain.models.weather.CurrentWeather
import com.example.weatherapp.domain.utils.getFormattedNow
import com.example.weatherapp.domain.utils.isDayTime
import com.example.weatherapp.ui.viewModel.LocationViewModel
import com.example.weatherapp.ui.viewModel.WeatherViewModel


class CurrentWeather : Fragment() {

    private lateinit var binding: FragmentCurrentWeatherBinding
    private val weatherViewModel: WeatherViewModel by activityViewModels()
    private val locationViewModel: LocationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false)

        binding.swipeRefreshLayout.setOnRefreshListener {
            weatherViewModel.cachedWeather = null
            weatherViewModel.cachedForecast = null
            tryFetchLocation()
        }

        weatherViewModel.isLoadingCurrent.observe(viewLifecycleOwner) { isLoading ->
            binding.progressLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.contentLayout.visibility = if (isLoading) View.GONE else View.VISIBLE
        }



        locationViewModel.locationLiveData.observe(viewLifecycleOwner){ location ->
            val (lat, lon) = location
            if (isInternetAvailable()){
                weatherViewModel.loadCurrentWeather(lat.toDouble(), lon.toDouble())
            }else{
                binding.contentLayout.visibility = View.GONE
                binding.offlineMessageLayout.visibility = View.VISIBLE
                binding.buttonRetry.setOnClickListener {
                    tryFetchLocation()
                }
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }

        weatherViewModel.currentWeather.observe(viewLifecycleOwner) { currentWeather ->
            currentWeather?.let {
                match(currentWeather)
                binding.progressLoading.visibility = View.GONE
                binding.contentLayout.visibility = View.VISIBLE
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }


        binding.buttonForecast.setOnClickListener {
            val forecastFragment = ForCastWeather()
            (activity as? MainActivity)?.loadFragment(forecastFragment, addToBackStack = true)
        }

        println("Cached weather at 1: ${weatherViewModel.cachedWeather}")


        return binding.root
    }


    private fun tryFetchLocation() {

        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationViewModel.fetchLocation()
            } else {
                showEnableGpsDialog()
                binding.swipeRefreshLayout.isRefreshing = false
            }

        if (isInternetAvailable()){
            binding.contentLayout.visibility = View.VISIBLE
            binding.offlineMessageLayout.visibility = View.GONE
        }

    }

    private fun showEnableGpsDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("GPS Disabled")
            .setMessage("GPS is required for this app to work properly. Would you like to enable it?")
            .setPositiveButton("Yes") { _, _ ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()


    }


    private fun isInternetAvailable(): Boolean {
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun match(weather: CurrentWeather){

        weatherViewModel.locationName.observe(viewLifecycleOwner) {
            binding.textLocation.text = weatherViewModel.extractRegion(it,4)
            binding.textRegion.text = weatherViewModel.extractRegion(it,3) + " / " + weatherViewModel.extractRegion(it,1)
        }
        binding.textSunset.text = weather.sunset
        binding.textsunrise.text = weather.sunrise
        binding.textMinMax.text = (weather.tempmax.toString() + "\u00B0C" + "/" + weather.tempmin.toString() + "\u00B0C") ?: "No data"
        binding.textDescription.text = weather.description
        binding.textCondition.text = weather.conditions
        binding.textDateTime.text = getFormattedNow()
        binding.textTemperature.text = (weather.feelsLike.toString() + " \u00B0C") ?: "No data"
        binding.textHumidity.text = (weather.humidity.toString() + "%") ?: "No data"
        binding.textWindSpeed.text = (weather.windSpeed.toString() + " km/h") ?: "No data"
        binding.textPressure.text = (weather.pressure.toString() + " hPa") ?: "No data"
        binding.textVisibility.text = (weather.visibility.toString() + " km") ?: "No data"
        binding.textCloudCover.text = (weather.cloudCover.toString() + "%") ?: "No data"
        binding.textUVIndex.text = (weather.uvIndex.toString()) ?: "No data"

        if (isDayTime()) {
            when(weather.conditions) {
                "Clear" -> {
                    binding.imageWeatherIcon.setImageResource(R.drawable.clear)
                }
                "Cloudy" -> {
                    binding.imageWeatherIcon.setImageResource(R.drawable.cloudy)
                }
                "Rain" -> {
                    binding.imageWeatherIcon.setImageResource(R.drawable.rainy)
                }
                else -> {
                    binding.imageWeatherIcon.setImageResource(R.drawable.sun_rainy)

                }
            }
        } else {
            when(weather.conditions) {
                "Clear" -> {
                    binding.imageWeatherIcon.setImageResource(R.drawable.clear_night)
                }
                "Cloudy" -> {
                    binding.imageWeatherIcon.setImageResource(R.drawable.cloudy_night)
                }
                "Rain" -> {
                    binding.imageWeatherIcon.setImageResource(R.drawable.rainy_night)
                }
                else -> {
                    binding.imageWeatherIcon.setImageResource(R.drawable.cloudy_night)
                }
            }
        }


    }


}