package com.codingchallenge.weather.data.remote

import com.codingchallenge.weather.BuildConfig
import com.codingchallenge.weather.data.local.USStateUtils
import com.codingchallenge.weather.data.models.WeatherData
import com.codingchallenge.weather.domain.repository.WeatherRepository
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi
) : WeatherRepository {

    private val apiKey = BuildConfig.OPEN_WEATHER_API_KEY

    override suspend fun getWeatherByCity(cityName: String, units: String): Result<WeatherData> {
        return try {
            val trimmedName = cityName.trim()
            
            // Check if input is a ZIP code (5 digits)
            val coords = if (trimmedName.matches(Regex("^\\d{5}$"))) {
                weatherApi.getCoordinatesByZip("$trimmedName,US", apiKey)
            } else {
                // City Name Search
                val query = when {
                    trimmedName.contains(",US", ignoreCase = true) -> trimmedName
                    trimmedName.count { it == ',' } == 1 -> "$trimmedName,US"
                    !trimmedName.contains(",") -> "$trimmedName,US"
                    else -> trimmedName
                }
                val coordsList = weatherApi.getCoordinatesByCity(query, 1, apiKey)
                if (coordsList.isNotEmpty()) coordsList[0] else null
            }

            if (coords != null) {
                val weather = weatherApi.getWeatherByCoordinates(coords.lat, coords.lon, units = units, apiKey = apiKey)
                weather.state = USStateUtils.getAbbreviation(coords.state)
                Result.success(weather)
            } else {
                Result.failure(Exception("City or ZIP code not found"))
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Result.failure(Exception(errorBody ?: e.message()))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWeatherByLocation(lat: Double, lon: Double, units: String): Result<WeatherData> {
        return try {
            val weather = weatherApi.getWeatherByCoordinates(lat, lon, units = units, apiKey = apiKey)
            
            // Gracious Fallback: If reverse geocoding fails, we still return the weather data.
            try {
                val geoList = weatherApi.getReverseGeocode(lat, lon, 1, apiKey)
                if (geoList.isNotEmpty()) {
                    weather.state = USStateUtils.getAbbreviation(geoList[0].state)
                }
            } catch (e: Exception) {
                // Ignore geocoding failure for location-based weather
            }

            Result.success(weather)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Result.failure(Exception(errorBody ?: e.message()))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
