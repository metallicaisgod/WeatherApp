package com.kirillm.weatherapp.domain.repository

import com.kirillm.weatherapp.domain.entity.Forecast
import com.kirillm.weatherapp.domain.entity.Weather

interface WeatherRepository {

    suspend fun getWeather(cityId: Int): Weather

    suspend fun getForecast(cityId: Int): Forecast
}
