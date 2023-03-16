package com.example.transweather.features.search.data.repository

import com.example.transweather.core.repository.BaseRepository
import com.example.transweather.core.state.State
import com.example.transweather.features.common.services.OpenWeatherService
import com.example.transweather.features.search.data.model.response.LocationResponseDto
import com.example.transweather.features.search.domain.repository.LocationByZipRepository
import javax.inject.Inject

class LocationByZipRepositoryImpl @Inject constructor(private val service: OpenWeatherService) :
    BaseRepository<String, LocationResponseDto>(), LocationByZipRepository {


    override suspend fun getLocationByZip(zip: String): State<LocationResponseDto> {
        return getApiCallState(zip)
    }

    override suspend fun performApiCall(requestDto: String): State<LocationResponseDto> {
        val response = service.getLocationByZip(requestDto)
        return handleResponse(response)
    }

}