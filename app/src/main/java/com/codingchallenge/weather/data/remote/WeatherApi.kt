package com.codingchallenge.weather.data.remote

import com.codingchallenge.weather.data.models.CityCoordinates
import com.codingchallenge.weather.data.models.WeatherData
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("geo/1.0/direct")
    suspend fun getCoordinatesByCity(
        @Query("q") cityName: String,
        @Query("limit") limit: Int = 1,
        @Query("appid") apiKey: String
    ): List<CityCoordinates>

    @GET("geo/1.0/zip")
    suspend fun getCoordinatesByZip(
        @Query("zip") zipCode: String,
        @Query("appid") apiKey: String
    ): CityCoordinates

    @GET("data/2.5/weather")
    suspend fun getWeatherByCoordinates(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("appid") apiKey: String
    ): WeatherData

    @GET("geo/1.0/reverse")
    suspend fun getReverseGeocode(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("limit") limit: Int = 1,
        @Query("appid") apiKey: String
    ): List<CityCoordinates>
}
