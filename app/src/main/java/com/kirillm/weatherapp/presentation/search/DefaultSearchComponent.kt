package com.kirillm.weatherapp.presentation.search

import com.arkivanov.decompose.ComponentContext

class DefaultSearchComponent(
    private val componentContext: ComponentContext
) : SearchComponent, ComponentContext by componentContext