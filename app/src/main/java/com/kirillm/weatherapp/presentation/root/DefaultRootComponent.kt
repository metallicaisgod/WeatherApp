package com.kirillm.weatherapp.presentation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.kirillm.weatherapp.domain.entity.City
import com.kirillm.weatherapp.presentation.details.DefaultDetailsComponent
import com.kirillm.weatherapp.presentation.favourites.DefaultFavouritesComponent
import com.kirillm.weatherapp.presentation.search.DefaultSearchComponent
import com.kirillm.weatherapp.presentation.search.SearchReason
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.serialization.Serializable

class DefaultRootComponent @AssistedInject constructor(
    private val detailsComponentFactory: DefaultDetailsComponent.Factory,
    private val favouritesComponentFactory: DefaultFavouritesComponent.Factory,
    private val searchComponentFactory: DefaultSearchComponent.Factory,
    @Assisted("componentContext") private val componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Favourites,
        handleBackButton = true,
        childFactory = ::child
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext,
    ): RootComponent.Child {
        return when (config) {
            is Config.Details -> {
                val component = detailsComponentFactory.create(
                    city = config.city,
                    onBackClicked = {
                        navigation.pop()
                    },
                    componentContext = componentContext
                )
                RootComponent.Child.Details(component)
            }

            Config.Favourites -> {
                val component = favouritesComponentFactory.create(
                    componentContext = componentContext,
                    onAddToFavouriteClicked = {
                        navigation.push(Config.Search(SearchReason.AddToFavourite))
                    },
                    onSearchFieldClicked = {
                        navigation.push(Config.Search(SearchReason.ShowForecast))
                    },
                    onFavouriteClicked = {
                        navigation.push(Config.Details(it))
                    }
                )
                RootComponent.Child.Favorite(component)
            }

            is Config.Search -> {
                val component = searchComponentFactory.create(
                    searchReason = config.searchReason,
                    componentContext = componentContext,
                    onBackClicked = {
                        navigation.pop()
                    },
                    onOpenedForecast = {
                        navigation.push(Config.Details(it))
                    },
                    onSavedToFavourite = {
                        navigation.pop()
                    }

                )
                RootComponent.Child.Search(component)
            }
        }
    }

    @Serializable
    sealed interface Config {

        @Serializable
        data object Favourites : Config

        @Serializable
        data class Details(val city: City) : Config

        @Serializable
        data class Search(val searchReason: SearchReason) : Config
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
        ): DefaultRootComponent

    }
}