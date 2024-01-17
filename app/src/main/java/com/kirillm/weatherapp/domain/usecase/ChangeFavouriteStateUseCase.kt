package com.kirillm.weatherapp.domain.usecase

import com.kirillm.weatherapp.domain.entity.City
import com.kirillm.weatherapp.domain.repository.FavouriteRepository
import javax.inject.Inject

class ChangeFavouriteStateUseCase @Inject constructor(
    private val repository: FavouriteRepository
) {

    suspend fun addToFavourite(city: City) = repository.addToFavourite(city)
    suspend fun removeFromFavourite(cityId: Int) = repository.removeFromFavourite(cityId)
}
