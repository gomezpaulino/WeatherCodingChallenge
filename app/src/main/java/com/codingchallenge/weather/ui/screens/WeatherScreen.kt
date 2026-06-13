package com.codingchallenge.weather.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.codingchallenge.weather.BuildConfig
import com.codingchallenge.weather.data.local.DateUtils
import com.codingchallenge.weather.data.models.WeatherData
import com.codingchallenge.weather.ui.viewmodel.WeatherUiState
import com.codingchallenge.weather.ui.viewmodel.WeatherViewModel

@Composable
fun WeatherScreen(viewModel: WeatherViewModel, modifier: Modifier = Modifier) {
    val uiState by viewModel.uiState.collectAsState()
    var cityName by remember { mutableStateOf("") }
    val unitSymbol = if (viewModel.units == "metric") "°C" else "°F"
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    val onSearchAction = {
        if (cityName.isNotBlank()) {
            viewModel.searchCity(cityName)
            focusManager.clearFocus()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = cityName,
            onValueChange = { cityName = it },
            label = { Text("Enter US City or ZIP") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(onSearch = { onSearchAction() })
        )

        Spacer(modifier = Modifier.height(24.dp))

        when (val state = uiState) {
            is WeatherUiState.Loading -> CircularProgressIndicator(modifier = Modifier.padding(top = 32.dp))
            is WeatherUiState.Success -> WeatherDetails(state.weather, unitSymbol)
            is WeatherUiState.Error -> ErrorState(state.message)
            is WeatherUiState.Info -> InfoState(state.message)
            is WeatherUiState.Idle -> IdleState()
        }
    }
}

@Composable
fun WeatherDetails(weather: WeatherData, unitSymbol: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val displayName = buildString {
                append(weather.cityName ?: "Unknown")
                if (!weather.state.isNullOrBlank()) {
                    append(", ")
                    append(weather.state)
                }
            }
            Text(
                text = displayName,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            val temp = weather.main?.temperature?.toInt() ?: 0
            Text(
                text = "$temp$unitSymbol",
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 80.sp),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Thin
            )

            val weatherInfo = weather.weather?.firstOrNull()
            if (weatherInfo != null) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    Color.Transparent
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = "${BuildConfig.IMAGE_URL}${weatherInfo.icon}@4x.png",
                        contentDescription = weatherInfo.description,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
                Text(
                    text = (weatherInfo.description ?: "").replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            val feelsLike = weather.main?.feelsLike?.toInt() ?: 0
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Feels like $feelsLike$unitSymbol",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.outline
            )

            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                WeatherStat(
                    icon = Icons.Default.WaterDrop,
                    label = "Humidity",
                    value = "${weather.main?.humidity ?: 0}%"
                )
                WeatherStat(
                    icon = Icons.Default.Air,
                    label = "Wind",
                    value = "${weather.wind?.speed?.toInt() ?: 0} ${if (unitSymbol == "°C") "km/h" else "mph"}"
                )
                val high = weather.main?.tempMax?.toInt() ?: 0
                val low = weather.main?.tempMin?.toInt() ?: 0
                WeatherStat(
                    icon = Icons.Default.DeviceThermostat,
                    label = "H/L",
                    value = "$high°/$low°"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val sunrise = weather.sys?.sunrise ?: 0
                val sunset = weather.sys?.sunset ?: 0
                WeatherStat(
                    icon = Icons.Default.WbSunny,
                    label = "Sunrise",
                    value = DateUtils.formatTime(sunrise)
                )
                WeatherStat(
                    icon = Icons.Default.WbTwilight,
                    label = "Sunset",
                    value = DateUtils.formatTime(sunset)
                )
                val visibilityKm = (weather.visibility / 1000.0)
                val visibilityMi = visibilityKm * 0.621371
                val visibilityDisplay = if (unitSymbol == "°C") {
                    "${(visibilityKm * 10).toInt() / 10.0} km"
                } else {
                    "${(visibilityMi * 10).toInt() / 10.0} mi"
                }
                WeatherStat(
                    icon = Icons.Default.Visibility,
                    label = "Visibility",
                    value = visibilityDisplay
                )
            }
        }
    }
}

@Composable
fun WeatherStat(icon: ImageVector, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
    }
}

@Composable
fun IdleState() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(top = 40.dp)) {
        Icon(Icons.Default.DeviceThermostat, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.outlineVariant)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Search for a city to see the weather", color = MaterialTheme.colorScheme.outline)
    }
}

@Composable
fun ErrorState(message: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 40.dp, start = 24.dp, end = 24.dp)
    ) {
        Icon(
            imageVector = Icons.Default.SearchOff,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "City Not Found",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun InfoState(message: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 40.dp, start = 24.dp, end = 24.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center
        )
    }
}
