package com.kirillm.weatherapp

import android.app.Application
import com.kirillm.weatherapp.di.ApplicationComponent
import com.kirillm.weatherapp.di.DaggerApplicationComponent

class WeatherApp : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
       applicationComponent = DaggerApplicationComponent.factory().create(this)
    }
}
