package com.kirillm.weatherapp.presentation.favourites

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.kirillm.weatherapp.domain.entity.City
import com.kirillm.weatherapp.presentation.componentScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DefaultFavouritesComponent @Inject constructor(
    private val storeFactory: FavouritesStoreFactory,
    private val componentContext: ComponentContext,
    private val onAddToFavouriteClick: () -> Unit,
    private val onSearchClick: () -> Unit,
    private val onFavouriteClick: (City) -> Unit
) : FavouritesComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect{
                when(it) {
                    FavouritesStore.Label.OnAddFavoriteClicked -> {
                        onAddToFavouriteClick()
                    }
                    is FavouritesStore.Label.OnFavouriteClicked -> {
                        onFavouriteClick(it.city)
                    }
                    FavouritesStore.Label.OnSearchClicked -> {
                        onSearchClick()
                    }
                }
            }
        }
    }

    override val model: StateFlow<FavouritesStore.State>
        get() = store.stateFlow

    override fun onSearchClicked() {
        store.accept(FavouritesStore.Intent.OnSearchClicked)
    }

    override fun onAddFavourite() {
        store.accept(FavouritesStore.Intent.OnAddFavoriteClicked)
    }

    override fun onCityClicked(city: City) {
        store.accept(FavouritesStore.Intent.OnFavouriteClicked(city))
    }
}