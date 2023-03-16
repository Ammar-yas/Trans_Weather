package com.example.transweather.features.search.domain.repository

import com.example.transweather.core.state.State
import com.example.transweather.features.search.data.model.response.LocationResponseDto

interface LocationByLatAndLongRepository {
    suspend fun getLocationByLatAndLong(requestDto: Map<String,String>): State<List<LocationResponseDto>>
}