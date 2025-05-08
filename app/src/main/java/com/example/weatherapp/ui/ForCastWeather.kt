package com.example.weatherapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.databinding.FragmentForCastWeatherBinding
import com.example.weatherapp.domain.utils.getFormattedNow
import com.example.weatherapp.ui.viewModel.LocationViewModel
import com.example.weatherapp.ui.viewModel.WeatherViewModel
import kotlin.getValue


class ForCastWeather : Fragment() {

    private lateinit var binding: FragmentForCastWeatherBinding
    private val weatherViewModel: WeatherViewModel by activityViewModels()
    private val locationViewModel: LocationViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForCastWeatherBinding.inflate(inflater, container, false)

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        weatherViewModel.isLoadingForecast.observe(viewLifecycleOwner) { isLoading ->
            binding.progressLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.recyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        locationViewModel.locationLiveData.observe(viewLifecycleOwner) { location ->
            val (lat, lon) = location
            weatherViewModel.loadFiveDayForecast(lat.toDouble(), lon.toDouble())
        }

        binding.todayDate.text = getFormattedNow()


        weatherViewModel.fiveDayForecast.observe(viewLifecycleOwner) { forecastWeather ->
            forecastWeather?.forecasts?.let { forecasts ->
                val adapter = ForecastAdapter(forecasts)
                binding.recyclerView.adapter = adapter
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerView.addItemDecoration(
                    androidx.recyclerview.widget.DividerItemDecoration(
                        requireContext(),
                        LinearLayoutManager.VERTICAL
                    )
                )


            }
        }

        println("Cachedforcast at 2: ${weatherViewModel.cachedForecast}")





        return binding.root
    }


}