package com.kirillm.weatherapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class CityDto(
    @SerializedName("Key") val id: Int,
    @SerializedName("LocalizedName") val name: String,
    @SerializedName("Country") val cityCountry: Country
) {
    val country: String
        get() = cityCountry.country
}

data class Country(
    @SerializedName("LocalizedName") val country: String
)
