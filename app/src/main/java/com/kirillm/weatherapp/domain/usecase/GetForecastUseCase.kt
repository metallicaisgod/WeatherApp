package com.kirillm.weatherapp.domain.usecase

import com.kirillm.weatherapp.domain.entity.Forecast
import com.kirillm.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {

    suspend operator fun invoke(cityId: Int) = Forecast (
        currentWeather = repository.getWeather(cityId),
        upcoming = repository.getForecast(cityId)
    )
}
