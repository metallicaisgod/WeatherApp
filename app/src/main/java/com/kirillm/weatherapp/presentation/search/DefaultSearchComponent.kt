package com.kirillm.weatherapp.presentation.search

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.kirillm.weatherapp.domain.entity.City
import com.kirillm.weatherapp.presentation.componentScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DefaultSearchComponent @Inject constructor(
    private val searchReason: SearchReason,
    private val storeFactory: SearchStoreFactory,
    private val componentContext: ComponentContext,
    private val onBackClick: () -> Unit,
    private val onOpenForecast: (City) -> Unit,
    private val onSaveToFavourite: () -> Unit,
) : SearchComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create(searchReason) }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect{
                when(it){
                    SearchStore.Label.ClickBack -> {
                        onBackClick()
                    }
                    is SearchStore.Label.OpenForecast -> {
                        onOpenForecast(it.city)
                    }
                    SearchStore.Label.SavedToFavourite -> {
                        onSaveToFavourite()
                    }
                }
            }
        }
    }

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
}