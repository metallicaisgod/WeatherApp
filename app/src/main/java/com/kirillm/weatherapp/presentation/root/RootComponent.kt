package com.kirillm.weatherapp.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.kirillm.weatherapp.presentation.details.DetailsComponent
import com.kirillm.weatherapp.presentation.favourites.FavouritesComponent
import com.kirillm.weatherapp.presentation.search.SearchComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {

        data class Favorite(val component: FavouritesComponent) : Child

        data class Details(val component: DetailsComponent) : Child

        data class Search(val component: SearchComponent) : Child
    }
}