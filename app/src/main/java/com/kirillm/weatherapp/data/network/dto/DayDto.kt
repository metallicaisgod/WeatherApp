package com.kirillm.weatherapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class DayDto(
    @SerializedName("EpochDate") val date: Long,
    @SerializedName("Temperature") val temperature: Temperature,
    @SerializedName("Day") val dayCondition: DayCondition,

) {
    val dayWeatherDto: DayWeatherDto
        get() = DayWeatherDto(
            tempC = temperature.metric.temp,
            conditionDto = ConditionDto(
                iconUrl = dayCondition.icon.iconToUrlString(),
                text = dayCondition.iconPhrase
            )
        )
}

data class DayCondition (
    @SerializedName("Icon") val icon: Int,
    @SerializedName("IconPhrase") val iconPhrase: String,
)
