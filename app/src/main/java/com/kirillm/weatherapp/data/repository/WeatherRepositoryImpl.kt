package com.kirillm.weatherapp.data.repository

import com.kirillm.weatherapp.data.mapper.toEntity
import com.kirillm.weatherapp.data.network.api.ApiService
import com.kirillm.weatherapp.domain.entity.Forecast
import com.kirillm.weatherapp.domain.entity.Weather
import com.kirillm.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : WeatherRepository {
    override suspend fun getWeather(cityId: Int): Weather {
        return apiService.loadCurrentWeather(cityId).toEntity()
    }

    override suspend fun getForecast(cityId: Int): List<Weather> {
        return apiService.loadForecast(cityId).toEntity()
    }
}