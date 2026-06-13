# Weather Coding Challenge

A modern Android weather application built for a coding challenge. The app allows users to search for weather by US city name or ZIP code, and provides automatic weather updates based on current GPS location.

## 🚀 Features
- **Weather Search**: Search by City Name (e.g., "Bay Shore, NY") or ZIP Code (e.g., "11706").
- **Auto-Location**: Fetches weather for the current location on startup (requires user permission).
- **Persistence**: Remembers the last searched city, theme preference, and measurement units across app launches.
- **Unit Switching**: Toggle between Celsius (°C) and Fahrenheit (°F).
- **Light/Dark Mode**: Manual toggle with system-matching status bar icons and persistent state.
- **Modern UI**: Built entirely with Jetpack Compose and Material 3.

## 🛠 Tech Stack & Architecture
- **Language**: Kotlin (Architecture, UI, Logic) & Java (Models, Persistence).
- **Architecture**: MVVM with Clean Architecture principles.
- **Dependency Injection**: Pure Dagger 2 (Multibindings with `ViewModelFactory`).
- **Networking**: Retrofit 2 with Gson converter.
- **Async**: Kotlin Coroutines & Flow.
- **Image Loading**: Coil (with automatic memory and disk caching).
- **Location**: Google Play Services (Fused Location Provider).
- **Testing**: JUnit 4 & Mockito (Unit tests for ViewModel logic).

## 🏗 Key Design Decisions
- **Geocoding API**: Per the challenge requirements, direct weather search by city name is deprecated. This app implements the modern two-step approach:
  1. Use the **Geocoding API** to resolve input (City or ZIP) to coordinates.
  2. Use coordinates to fetch precise weather data.
- **Defensive Coding**: 
  - Race conditions are prevented by cancelling previous search jobs when a new search is initiated.
  - Manual searches prioritize over background location updates to ensure a stable user experience.
  - Graceful handling of network failures and permission denials.
- **Separation of Concerns**: The logic is strictly separated into `data`, `domain`, and `ui` layers to ensure maintainability and testability.

## ⚙️ How to Run
1. Clone the repository.
2. The `local.properties` file is included with a valid `OPEN_WEATHER_API_KEY` for immediate testing.
3. Sync Gradle and run the `app` module.
4. Run `./gradlew test` to execute the unit test suite.
