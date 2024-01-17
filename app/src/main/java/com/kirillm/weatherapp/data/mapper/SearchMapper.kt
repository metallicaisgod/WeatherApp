package com.kirillm.weatherapp.data.mapper

import com.kirillm.weatherapp.data.network.dto.CityDto
import com.kirillm.weatherapp.domain.entity.City

fun CityDto.toEntity(): City = City(id, name, country)

fun List<CityDto>.toEntities(): List<City> = map { it.toEntity() }
