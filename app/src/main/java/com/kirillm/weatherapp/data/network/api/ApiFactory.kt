package com.kirillm.weatherapp.data.network.api

import com.kirillm.weatherapp.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ApiFactory {

    private const val KAY_PARAM = "key"
    private const val BASE_URL = "https://api.weatherapi.com/v1/"

    private val okHttpClient = OkHttpClient().newBuilder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val newUrl = originalRequest
                .url()
                .newBuilder()
                .addQueryParameter(KAY_PARAM, BuildConfig.WEATHER_API_KEY)
                .build()
            val newRequest = originalRequest
                .newBuilder()
                .url(newUrl)
                .build()
            chain.proceed(newRequest)
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create()
}
