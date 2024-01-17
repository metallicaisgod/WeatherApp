package com.kirillm.weatherapp.presentation.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.kirillm.weatherapp.domain.entity.City
import com.kirillm.weatherapp.presentation.componentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DefaultDetailsComponent @Inject constructor(
    private val city: City,
    private val storeFactory: DetailsStoreFactory,
    private val componentContext: ComponentContext,
    private val onBackClick: () -> Unit,
) : DetailsComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create(city) }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    DetailsStore.Label.ClickBack -> onBackClick()
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<DetailsStore.State>
        get() = store.stateFlow

    override fun onClickBack() {
        store.accept(DetailsStore.Intent.ClickBack)
    }

    override fun onFavouriteClick() {
        store.accept(DetailsStore.Intent.ClickFavourite)
    }
}