package com.codingchallenge.weather.di

import com.codingchallenge.weather.data.remote.WeatherRepositoryImpl
import com.codingchallenge.weather.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        weatherRepositoryImpl: WeatherRepositoryImpl
    ): WeatherRepository
}
