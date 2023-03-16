package com.example.transweather.features.search.data.model.repository

import com.example.transweather.core.repository.BaseRepository
import com.example.transweather.core.state.State
import com.example.transweather.features.common.services.OpenWeatherService
import com.example.transweather.features.search.data.model.request.LocationByNameRequestDto
import com.example.transweather.features.search.data.model.response.LocationResponseDto
import com.example.transweather.features.search.domain.repository.LocationByNameRepository
import javax.inject.Inject

class LocationByNameRepositoryImpl @Inject constructor(private val service: OpenWeatherService) :
    BaseRepository<LocationByNameRequestDto, List<LocationResponseDto>>(), LocationByNameRepository {


    override suspend fun getLocationByName(requestDto: LocationByNameRequestDto): State<List<LocationResponseDto>> {
        return getApiCallState(requestDto)
    }

    override suspend fun performApiCall(requestDto: LocationByNameRequestDto): State<List<LocationResponseDto>> {
        val response = service.getLocationByName(requestDto.queryMap)
        return handleResponse(response)
    }



}