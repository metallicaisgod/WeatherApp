package com.kirillm.weatherapp.presentation.details

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

class DefaultDetailsComponent @AssistedInject constructor(
    private val storeFactory: DetailsStoreFactory,
    @Assisted("city") private val city: City,
    @Assisted("componentContext") private val componentContext: ComponentContext,
    @Assisted("onBackClicked") private val onBackClicked: () -> Unit,
) : DetailsComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create(city) }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    DetailsStore.Label.ClickBack -> onBackClicked()
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

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("city") city: City,
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("onBackClicked") onBackClicked: () -> Unit,
        ): DefaultDetailsComponent
    }
}