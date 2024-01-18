package com.kirillm.weatherapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class WeatherCurrentDto(
    @SerializedName("EpochTime") val date: Long,
    @SerializedName("Temperature") val temperature: Temperature,
    @SerializedName("WeatherText") val weatherText: String,
    @SerializedName("WeatherIcon") val weatherIcon: Int,
) {

    val current: WeatherDto
        get() = WeatherDto(
            date = date,
            tempC = tempC,
            conditionDto = conditionDto
        )

    private val tempC: Float
        get() = temperature.metric.temp

    private val conditionDto: ConditionDto
        get() = ConditionDto(
            text = weatherText,
            iconUrl = weatherIcon.iconToUrlString()
        )
}

data class Temperature(
    @SerializedName("Metric") val metric: Metric,
)

data class Metric(
    @SerializedName("Value") val temp: Float,
)
