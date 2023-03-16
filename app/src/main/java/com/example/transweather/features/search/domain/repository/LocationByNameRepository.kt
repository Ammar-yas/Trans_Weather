package com.example.transweather.features.search.domain.repository

import com.example.transweather.core.state.State
import com.example.transweather.features.search.data.model.request.LocationByNameRequestDto
import com.example.transweather.features.search.data.model.response.LocationResponseDto

interface LocationByNameRepository {
    suspend fun getLocationByName(requestDto: LocationByNameRequestDto): State<List<LocationResponseDto>>
}