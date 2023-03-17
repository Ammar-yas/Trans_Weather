package com.example.transweather.features.current_weather.domain.model

import com.example.transweather.features.common.models.Unit
import com.example.transweather.features.search.data.model.response.LocationResponseDto

class CurrentWeatherUIModel(val locationResponseDto: LocationResponseDto) {

    var name = locationResponseDto.name
    var iconUrl = ""
    var temp = ""
    var main = ""
    var description = ""
    var unit: Unit = Unit.Celsius

}