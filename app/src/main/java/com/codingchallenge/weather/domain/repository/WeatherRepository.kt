package com.codingchallenge.weather.domain.repository

import com.codingchallenge.weather.data.models.WeatherData

interface WeatherRepository {
    suspend fun getWeatherByCity(cityName: String, units: String): Result<WeatherData>
    suspend fun getWeatherByLocation(lat: Double, lon: Double, units: String): Result<WeatherData>
}
