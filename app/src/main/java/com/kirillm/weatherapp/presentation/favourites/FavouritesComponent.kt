package com.kirillm.weatherapp.presentation.favourites

import com.kirillm.weatherapp.domain.entity.City
import kotlinx.coroutines.flow.StateFlow

interface FavouritesComponent {

    val model: StateFlow<FavouritesStore.State>

    fun onSearchClicked()

    fun onAddFavourite()

    fun onCityClicked(city: City)
}