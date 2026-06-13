package com.codingchallenge.weather

import android.app.Application
import com.codingchallenge.weather.di.AppComponent
import com.codingchallenge.weather.di.DaggerAppComponent

class WeatherApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
    }
}
