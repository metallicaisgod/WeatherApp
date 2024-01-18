package com.kirillm.weatherapp.data.network.dto

fun Int.iconToUrlString() =
    "https://developer.accuweather.com/sites/default/files/%02d-s.png".format(this)