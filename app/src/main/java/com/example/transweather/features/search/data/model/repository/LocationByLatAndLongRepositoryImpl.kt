package com.example.transweather.features.search.data.model.repository

import com.example.transweather.core.repository.BaseRepository
import com.example.transweather.core.state.State
import com.example.transweather.features.common.services.OpenWeatherService
import com.example.transweather.features.search.data.model.request.LocationByNameRequestDto
import com.example.transweather.features.search.data.model.response.LocationResponseDto
import com.example.transweather.features.search.domain.repository.LocationByLatAndLongRepository
import javax.inject.Inject

class LocationByLatAndLongRepositoryImpl @Inject constructor(private val service: OpenWeatherService) :
    BaseRepository<Map<String, String>, List<LocationResponseDto>>(),
    LocationByLatAndLongRepository {

    override suspend fun performApiCall(requestDto: Map<String, String>): State<List<LocationResponseDto>> {
        return getApiCallState(requestDto)
    }

    override suspend fun getLocationByLatAndLong(requestDto: Map<String, String>): State<List<LocationResponseDto>> {
        val response = service.getLocationByLatAndLong(requestDto)
        return handleResponse(response)
    }

}