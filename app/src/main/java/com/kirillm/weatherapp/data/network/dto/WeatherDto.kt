package com.kirillm.weatherapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class WeatherDto(
    val date: Long,
    val tempC: Float,
    val conditionDto: ConditionDto
)
