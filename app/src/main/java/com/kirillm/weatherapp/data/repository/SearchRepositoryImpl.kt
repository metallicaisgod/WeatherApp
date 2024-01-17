package com.kirillm.weatherapp.data.repository

import com.kirillm.weatherapp.data.mapper.toEntities
import com.kirillm.weatherapp.data.network.api.ApiService
import com.kirillm.weatherapp.domain.entity.City
import com.kirillm.weatherapp.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : SearchRepository {
    override suspend fun search(query: String): List<City> {
        return apiService.searchCity(query).toEntities()
    }
}
