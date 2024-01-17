package com.kirillm.weatherapp.domain.repository

import com.kirillm.weatherapp.domain.entity.City

interface SearchRepository {

    suspend fun search(query: String): List<City>
}
