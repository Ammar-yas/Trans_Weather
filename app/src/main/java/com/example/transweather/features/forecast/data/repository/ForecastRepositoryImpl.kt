package com.example.transweather.features.forecast.data.repository

import com.example.transweather.core.repository.BaseRepository
import com.example.transweather.core.state.State
import com.example.transweather.features.common.services.OpenWeatherService
import com.example.transweather.features.forecast.data.model.response.ForecastDto
import com.example.transweather.features.forecast.domain.repository.ForecastRepository
import com.example.transweather.features.search.data.model.request.LocationByNameRequestDto
import com.example.transweather.features.search.data.model.response.LocationResponseDto
import com.example.transweather.features.search.domain.repository.LocationByLatAndLongRepository
import javax.inject.Inject

class ForecastRepositoryImpl @Inject constructor(private val service: OpenWeatherService) :
    BaseRepository<Map<String, String>, ForecastDto>(),
    ForecastRepository {

    override suspend fun getForecast(requestDto: Map<String, String>): State<ForecastDto> {
        return getApiCallState(requestDto)
    }

    override suspend fun performApiCall(requestDto: Map<String, String>): State<ForecastDto> {
        val response = service.getForecast(requestDto)
        return handleResponse(response)
    }




}