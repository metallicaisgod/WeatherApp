package com.kirillm.weatherapp.presentation.search

import com.kirillm.weatherapp.domain.entity.City
import kotlinx.coroutines.flow.StateFlow

interface SearchComponent {

    val model: StateFlow<SearchStore.State>

    fun onChangeQuery(query: String)

    fun onClickBack()

    fun onClickSearch()

    fun onClickCity(city: City)
}