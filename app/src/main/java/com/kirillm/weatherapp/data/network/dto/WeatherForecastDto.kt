package com.kirillm.weatherapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class WeatherForecastDto(
    @SerializedName("DailyForecasts") val forecastDay: List<DayDto>
) {
    val forecastDto: ForecastDto
        get() = ForecastDto(
            forecastDay = forecastDay
        )
}
