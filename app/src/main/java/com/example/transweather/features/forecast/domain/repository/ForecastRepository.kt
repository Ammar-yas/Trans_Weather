package com.example.transweather.features.forecast.domain.repository

import com.example.transweather.core.state.State
import com.example.transweather.features.forecast.data.model.response.ForecastDto

interface ForecastRepository {
    suspend fun getForecast(requestDto: Map<String, String>): State<ForecastDto>
}