package com.codingchallenge.weather.ui.viewmodel

import com.codingchallenge.weather.data.local.PreferenceManager
import com.codingchallenge.weather.data.models.WeatherData
import com.codingchallenge.weather.domain.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.never

@ExperimentalCoroutinesApi
class WeatherViewModelTest {

    @Mock
    lateinit var repository: WeatherRepository

    @Mock
    lateinit var preferenceManager: PreferenceManager

    private lateinit var viewModel: WeatherViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        
        // Default mocks
        `when`(preferenceManager.lastCity).thenReturn(null)
        `when`(preferenceManager.units).thenReturn("imperial")
        `when`(preferenceManager.isDarkMode).thenReturn(false)
        
        viewModel = WeatherViewModel(repository, preferenceManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `searchCity updates state to Success when repository returns success`() = runTest {
        val weatherData = WeatherData(
            "London",
            WeatherData.Main(20.0, 20.0, 15.0, 25.0, 50),
            listOf(WeatherData.Weather("Clouds", "overcast clouds", "04d"))
        )
        `when`(repository.getWeatherByCity(any(), any())).thenReturn(Result.success(weatherData))

        viewModel.searchCity("London")

        assertTrue(viewModel.uiState.value is WeatherUiState.Success)
        verify(preferenceManager).saveLastCity("London")
    }

    @Test
    fun `searchCity updates state to Error when repository returns failure`() = runTest {
        `when`(repository.getWeatherByCity(any(), any())).thenReturn(Result.failure(Exception("Error")))

        viewModel.searchCity("Unknown")

        assertTrue(viewModel.uiState.value is WeatherUiState.Error)
    }

    @Test
    fun `toggleDarkMode updates state and saves to preferences`() {
        viewModel.toggleDarkMode()
        assertTrue(viewModel.isDarkMode)
        verify(preferenceManager).saveDarkMode(true)

        viewModel.toggleDarkMode()
        assertTrue(!viewModel.isDarkMode)
        verify(preferenceManager).saveDarkMode(false)
    }

    @Test
    fun `toggleUnits updates state, saves to preferences, and refreshes data`() = runTest {
        // Initial success state to trigger refresh
        val weatherData = WeatherData("London", WeatherData.Main(20.0, 20.0, 15.0, 25.0, 50), emptyList())
        `when`(repository.getWeatherByCity(any(), any())).thenReturn(Result.success(weatherData))
        viewModel.searchCity("London")
        
        viewModel.toggleUnits()
        
        assertEquals("metric", viewModel.units)
        verify(preferenceManager).saveUnits("metric")
        // Verify that search is called again with metric units
        verify(repository).getWeatherByCity(eq("London"), eq("metric"))
    }

    @Test
    fun `fetchWeatherByLocation is ignored if manual search is active`() = runTest {
        val weatherData = WeatherData("London", WeatherData.Main(20.0, 20.0, 15.0, 25.0, 50), emptyList())
        `when`(repository.getWeatherByCity(any(), any())).thenReturn(Result.success(weatherData))

        viewModel.searchCity("London")
        
        viewModel.fetchWeatherByLocation(10.0, 10.0)
        
        verify(repository, never()).getWeatherByLocation(any(), any(), any())
    }

    @Test
    fun `onLocationPermissionDenied updates state to Info`() {
        viewModel.onLocationPermissionDenied()
        assertTrue(viewModel.uiState.value is WeatherUiState.Info)
    }
}
