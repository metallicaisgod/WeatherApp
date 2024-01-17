package com.kirillm.weatherapp.presentation.favourites

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.kirillm.weatherapp.domain.entity.City
import com.kirillm.weatherapp.domain.usecase.GetCurrentWeatherUseCase
import com.kirillm.weatherapp.domain.usecase.GetFavouriteCitiesUseCase
import com.kirillm.weatherapp.presentation.favourites.FavouritesStore.Intent
import com.kirillm.weatherapp.presentation.favourites.FavouritesStore.Label
import com.kirillm.weatherapp.presentation.favourites.FavouritesStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface FavouritesStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object OnSearchClicked : Intent

        data object OnAddFavoriteClicked : Intent

        data class OnFavouriteClicked(val city: City) : Intent
    }

    data class State(val cityItems: List<CityItem>) {

        data class CityItem(
            val city: City,
            val weatherState: WeatherState,
        )

        sealed interface WeatherState {

            data object Initial : WeatherState

            data object Loading : WeatherState

            data object Error : WeatherState

            data class Loaded(
                val tempC: Float,
                val conditionIconUrl: String,
            ) : WeatherState
        }
    }

    sealed interface Label {

        data object OnSearchClicked : Label

        data object OnAddFavoriteClicked : Label

        data class OnFavouriteClicked(val city: City) : Label
    }
}

class FavouritesStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getFavouriteCitiesUseCase: GetFavouriteCitiesUseCase,
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
) {

    fun create(): FavouritesStore =
        object : FavouritesStore, Store<Intent, State, Label> by storeFactory.create(
            name = "FavouritesStore",
            initialState = State(listOf()),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data class FavouriteCitiesLoaded(val cities: List<City>) : Action
    }

    private sealed interface Msg {

        data class FavouriteCitiesLoaded(val cities: List<City>) : Msg

        data class WeatherLoaded(
            val cityId: Int,
            val tempC: Float,
            val conditionIconUrl: String
        ) : Msg

        data class WeatherLoadingError(
            val cityId: Int
        ) : Msg

        data class WeatherIsLoading(
            val cityId: Int
        ) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getFavouriteCitiesUseCase().collect{
                    dispatch(Action.FavouriteCitiesLoaded(it))
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.OnFavouriteClicked -> {
                    publish(Label.OnFavouriteClicked(intent.city))
                }

                Intent.OnSearchClicked -> {
                    publish(Label.OnSearchClicked)
                }

                Intent.OnAddFavoriteClicked -> {
                    publish(Label.OnAddFavoriteClicked)
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when(action){
                is Action.FavouriteCitiesLoaded -> {
                    val cities = action.cities
                    dispatch(Msg.FavouriteCitiesLoaded(cities))
                    cities.forEach{
                        scope.launch {
                            loadWeatherForCity(it)
                        }
                    }
                }
            }
        }

        private suspend fun loadWeatherForCity(city: City) {
            dispatch(Msg.WeatherIsLoading(city.id))
            try {
                val weather = getCurrentWeatherUseCase(city.id)
                dispatch(Msg.WeatherLoaded(
                    cityId = city.id,
                    tempC = weather.tempC,
                    conditionIconUrl = weather.conditionUrl
                ))
            } catch (e: Exception) {
                dispatch(Msg.WeatherLoadingError(city.id))
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when(msg) {
            is Msg.FavouriteCitiesLoaded -> {
                copy(
                    cityItems = msg.cities.map {
                        State.CityItem(
                            city = it,
                            weatherState = State.WeatherState.Initial
                        )
                    }
                )
            }
            is Msg.WeatherIsLoading -> {
                copy(
                    cityItems = cityItems.map {
                        if(it.city.id == msg.cityId){
                            it.copy(weatherState = State.WeatherState.Loading)
                        } else {
                            it
                        }
                    }
                )
            }
            is Msg.WeatherLoaded -> {
                copy(
                    cityItems = cityItems.map {
                        if(it.city.id == msg.cityId){
                            it.copy(weatherState = State.WeatherState.Loaded(
                                tempC = msg.tempC,
                                conditionIconUrl = msg.conditionIconUrl
                            ))
                        } else {
                            it
                        }
                    }
                )
            }
            is Msg.WeatherLoadingError -> {
                copy(
                    cityItems = cityItems.map {
                        if(it.city.id == msg.cityId){
                            it.copy(weatherState = State.WeatherState.Error)
                        } else {
                            it
                        }
                    }
                )
            }
        }
    }
}
