package com.kirillm.weatherapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.arkivanov.decompose.defaultComponentContext
import com.kirillm.weatherapp.WeatherApp
import com.kirillm.weatherapp.domain.usecase.ChangeFavouriteStateUseCase
import com.kirillm.weatherapp.domain.usecase.SearchCityUseCase
import com.kirillm.weatherapp.presentation.root.DefaultRootComponent
import com.kirillm.weatherapp.presentation.root.RootContent
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var rootComponentFactory: DefaultRootComponent.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as WeatherApp).applicationComponent.inject(this)

        val rootComponent = rootComponentFactory.create(defaultComponentContext())

        setContent {
            RootContent(component = rootComponent)
        }
    }
}