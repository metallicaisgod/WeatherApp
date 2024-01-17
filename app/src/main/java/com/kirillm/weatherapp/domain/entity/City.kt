package com.kirillm.weatherapp.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class City(
    val id: Int,
    val name: String,
    val country: String,
)
