package com.example.transweather.features.current_weather.domain.repository

import com.example.transweather.core.state.State
import com.example.transweather.features.current_weather.data.model.response.CurrentWeatherDto

interface CurrentWeatherRepository {
    suspend fun getCurrentWeather(requestDto: Map<String, String>): State<CurrentWeatherDto>

}