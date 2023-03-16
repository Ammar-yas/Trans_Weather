package com.example.transweather.features.current_weather.domain.model

import com.example.transweather.features.current_weather.domain.use_case.CurrentWeatherUseCase
import com.example.transweather.features.search.data.model.response.LocationResponseDto

class CurrentWeatherUIModel(val locationResponseDto: LocationResponseDto) {

    var name = locationResponseDto.name
    var iconUrl = ""
    var temp = ""
    var main = ""
    var description = ""
    var unit: CurrentWeatherUseCase.Unit = CurrentWeatherUseCase.Unit.Celsius

}