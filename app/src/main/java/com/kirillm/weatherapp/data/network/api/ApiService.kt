package com.kirillm.weatherapp.data.network.api

import androidx.compose.ui.text.intl.Locale
import com.kirillm.weatherapp.data.network.dto.CityDto
import com.kirillm.weatherapp.data.network.dto.WeatherCurrentDto
import com.kirillm.weatherapp.data.network.dto.WeatherForecastDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("currentconditions/v1/{q}")
    suspend fun loadCurrentWeather(
        @Path("q") query: Int,
        @Query("language") locale: String = Locale.current.language
    ): WeatherCurrentDto

    @GET("forecasts/v1/daily/5day/{q}?metric=true")
    suspend fun loadForecast(
        @Path("q") query: Int,
        @Query("language") locale: String = Locale.current.language
    ): WeatherForecastDto

    @GET("locations/v1/cities/search")
    suspend fun searchCity(
        @Query("q") query: String,
        @Query("language") locale: String = Locale.current.language
    ): List<CityDto>
}
