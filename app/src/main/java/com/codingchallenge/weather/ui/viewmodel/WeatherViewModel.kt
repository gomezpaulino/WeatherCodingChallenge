package com.codingchallenge.weather.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingchallenge.weather.data.local.PreferenceManager
import com.codingchallenge.weather.data.models.WeatherData
import com.codingchallenge.weather.domain.repository.WeatherRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Idle)
    val uiState: StateFlow<WeatherUiState> = _uiState

    private var searchJob: Job? = null
    
    var isDarkMode by mutableStateOf(preferenceManager.isDarkMode)
        private set

    var units by mutableStateOf(preferenceManager.units) // "metric" or "imperial"
        private set

    private var isManualSearchActive = false

    init {
        loadLastCity()
    }

    fun toggleDarkMode() {
        isDarkMode = !isDarkMode
        preferenceManager.saveDarkMode(isDarkMode)
    }

    fun toggleUnits() {
        units = if (units == "metric") "imperial" else "metric"
        preferenceManager.saveUnits(units)
        refreshWeather()
    }

    private fun loadLastCity() {
        val lastCity = preferenceManager.lastCity
        if (lastCity != null) {
            isManualSearchActive = false 
            searchCity(lastCity, isFromAutoLoad = true)
        }
    }

    fun searchCity(cityName: String, isFromAutoLoad: Boolean = false) {
        if (cityName.isBlank()) {
            _uiState.value = WeatherUiState.Error("Please enter a city name")
            return
        }
        
        if (!isFromAutoLoad) {
            isManualSearchActive = true
        }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            repository.getWeatherByCity(cityName, units)
                .onSuccess { weather ->
                    if (!isFromAutoLoad) preferenceManager.saveLastCity(cityName)
                    _uiState.value = WeatherUiState.Success(weather)
                }
                .onFailure { error ->
                    val message = when (error) {
                        is UnknownHostException -> "No internet connection"
                        else -> error.message ?: "Unknown error"
                    }
                    _uiState.value = WeatherUiState.Error(message)
                }
        }
    }

    fun fetchWeatherByLocation(lat: Double, lon: Double) {
        if (isManualSearchActive) return 

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            repository.getWeatherByLocation(lat, lon, units)
                .onSuccess { weather ->
                    _uiState.value = WeatherUiState.Success(weather)
                }
                .onFailure { error ->
                    _uiState.value = WeatherUiState.Error(error.message ?: "Location search failed")
                }
        }
    }

    private fun refreshWeather() {
        val currentState = _uiState.value
        if (currentState is WeatherUiState.Success) {
            searchCity(currentState.weather.cityName, isFromAutoLoad = true)
        }
    }

    fun onLocationPermissionDenied() {
        _uiState.value = WeatherUiState.Info("Location permission denied. Please search manually.")
    }
}

sealed class WeatherUiState {
    data object Idle : WeatherUiState()
    data object Loading : WeatherUiState()
    data class Success(val weather: WeatherData) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
    data class Info(val message: String) : WeatherUiState()
}
