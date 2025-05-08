package com.example.weatherapp
import com.example.weatherapp.data.online.API
import com.example.weatherapp.data.online.WeatherApi
import com.example.weatherapp.domain.models.weather.CurrentWeather
import com.example.weatherapp.domain.models.weather.DailyForecast
import com.example.weatherapp.domain.models.weather.ForecastWeather
import com.example.weatherapp.domain.repositories.WeatherRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import io.mockk.*

class WeatherRepositoryTest {

    private lateinit var api: WeatherApi
    private lateinit var repository: WeatherRepository

    @Before
    fun setup() {
        api = mockk()
        repository = WeatherRepository(api)
    }

    @Test
    fun `getCurrentWeather returns data from API`() {
        val dummyWeather = CurrentWeather(
            temperature = 25.0,
            feelsLike = 24.0,
            humidity = 40.0,
            windSpeed = 10.0,
            pressure = 1012.0,
            visibility = 10.0,
            cloudCover = 0.0,
            uvIndex = 5,
            conditions = "Clear",
            icon = "clear-day",
            sunrise = "06:00:00",
            sunset = "18:00:00",
            latitude = 29.98,
            longitude = 31.29
        )
        every { api.fetchCurrentWeather(29.98, 31.29) } returns dummyWeather

        val result = repository.getCurrentWeather(29.98, 31.29)

        assertEquals(dummyWeather, result)
    }

    @Test
    fun `getFiveDayForecast returns forecast from API`() {
        val dummyForecast = ForecastWeather(
            forecasts = listOf(
                DailyForecast(
                    date = "2025-05-09",
                    icon = "sunny-icon",
                    humidity = 40.0,
                    windSpeed = 10.5,
                    pressure = 1015.0,
                    uvIndex = 7,
                    description = "Clear skies throughout the day",
                    tempMax = 21.5,
                    tempMin = 22.0,
                    tempAvg = 21.0
                ),
                DailyForecast(
                    date = "2025-05-09",
                    icon = "sunny-icon",
                    humidity = 40.0,
                    windSpeed = 10.5,
                    pressure = 1015.0,
                    uvIndex = 7,
                    description = "Clear skies throughout the day",
                    tempMax = 21.5,
                    tempMin = 22.0,
                    tempAvg = 21.0
                ),
                DailyForecast(
                    date = "2025-05-09",
                    icon = "sunny-icon",
                    humidity = 40.0,
                    windSpeed = 10.5,
                    pressure = 1015.0,
                    uvIndex = 7,
                    description = "Clear skies throughout the day",
                    tempMax = 21.5,
                    tempMin = 22.0,
                    tempAvg = 21.0
                )
            ),
            latitude = 29.98,
            longitude = 31.29
        )

        every { api.fetchFiveDayForecast(29.98, 31.29) } returns dummyForecast

        val result = repository.getFiveDayForecast(29.98, 31.29)

        assertEquals(dummyForecast, result)
    }
}
