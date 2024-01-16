package com.kirillm.weatherapp.presentation.favourites

import com.arkivanov.decompose.ComponentContext

class DefaultFavouritesComponent(
    private val componentContext: ComponentContext
) : FavouritesComponent, ComponentContext by componentContext