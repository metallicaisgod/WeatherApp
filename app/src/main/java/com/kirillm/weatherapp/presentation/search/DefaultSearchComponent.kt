package com.kirillm.weatherapp.presentation.search

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

class DefaultSearchComponent @AssistedInject constructor(
    private val storeFactory: SearchStoreFactory,
    @Assisted("searchReason") private val searchReason: SearchReason,
    @Assisted("componentContext") private val componentContext: ComponentContext,
    @Assisted("onBackClicked") private val onBackClicked: () -> Unit,
    @Assisted("onOpenedForecast") private val onOpenedForecast: (City) -> Unit,
    @Assisted("onSavedToFavourite") private val onSavedToFavourite: () -> Unit,
) : SearchComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create(searchReason) }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    SearchStore.Label.ClickBack -> {
                        onBackClicked()
                    }

                    is SearchStore.Label.OpenForecast -> {
                        onOpenedForecast(it.city)
                    }

                    SearchStore.Label.SavedToFavourite -> {
                        onSavedToFavourite()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<SearchStore.State>
        get() = store.stateFlow

    override fun onChangeQuery(query: String) {
        store.accept(SearchStore.Intent.ChangeSearchQuery(query))
    }

    override fun onClickBack() {
        store.accept(SearchStore.Intent.ClickBack)
    }

    override fun onClickSearch() {
        store.accept(SearchStore.Intent.ClickSearch)
    }

    override fun onClickCity(city: City) {
        store.accept(SearchStore.Intent.ClickCity(city))
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("searchReason") searchReason: SearchReason,
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("onBackClicked") onBackClicked: () -> Unit,
            @Assisted("onOpenedForecast") onOpenedForecast: (City) -> Unit,
            @Assisted("onSavedToFavourite") onSavedToFavourite: () -> Unit,
        ): DefaultSearchComponent
    }
}