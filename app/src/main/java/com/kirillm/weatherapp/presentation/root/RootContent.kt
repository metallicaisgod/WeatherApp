package com.kirillm.weatherapp.presentation.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.kirillm.weatherapp.presentation.details.DetailsContent
import com.kirillm.weatherapp.presentation.favourites.FavouritesContent
import com.kirillm.weatherapp.presentation.search.SearchContent
import com.kirillm.weatherapp.presentation.ui.theme.WeatherAppTheme

@Composable
fun RootContent(component: RootComponent) {

    WeatherAppTheme {
        Children(stack = component.stack) {
            when (val instance = it.instance) {
                is RootComponent.Child.Details -> {
                    DetailsContent(component = instance.component)
                }

                is RootComponent.Child.Favorite -> {
                    FavouritesContent(component = instance.component)
                }

                is RootComponent.Child.Search -> {
                    SearchContent(component = instance.component)
                }
            }
        }
    }
}