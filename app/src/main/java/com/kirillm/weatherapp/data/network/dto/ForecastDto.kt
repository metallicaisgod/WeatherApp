package com.kirillm.weatherapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class ForecastDto(
    @SerializedName("DailyForecasts") val forecastDay: List<DayDto>
)
