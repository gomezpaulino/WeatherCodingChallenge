package com.codingchallenge.weather.di

import android.content.Context
import com.codingchallenge.weather.MainActivity
import com.codingchallenge.weather.ui.viewmodel.WeatherViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, RepositoryModule::class, ViewModelModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(activity: MainActivity)
    
    fun weatherViewModel(): WeatherViewModel
}
