package com.kirillm.weatherapp.presentation.favourites

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.kirillm.weatherapp.domain.entity.City
import com.kirillm.weatherapp.presentation.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultFavouritesComponent @AssistedInject constructor(
    private val storeFactory: FavouritesStoreFactory,
    @Assisted("componentContext") private val componentContext: ComponentContext,
    @Assisted("onAddToFavouriteClicked") private val onAddToFavouriteClicked: () -> Unit,
    @Assisted("onSearchFieldClicked") private val onSearchFieldClicked: () -> Unit,
    @Assisted("onFavouriteClicked") private val onFavouriteClicked: (City) -> Unit,
) : FavouritesComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    FavouritesStore.Label.OnAddFavoriteClicked -> {
                        onAddToFavouriteClicked()
                    }

                    is FavouritesStore.Label.OnFavouriteClicked -> {
                        onFavouriteClicked(it.city)
                    }

                    FavouritesStore.Label.OnSearchClicked -> {
                        onSearchFieldClicked()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
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

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("onAddToFavouriteClicked") onAddToFavouriteClicked: () -> Unit,
            @Assisted("onSearchFieldClicked") onSearchFieldClicked: () -> Unit,
            @Assisted("onFavouriteClicked") onFavouriteClicked: (City) -> Unit,
        ): DefaultFavouritesComponent
    }
}