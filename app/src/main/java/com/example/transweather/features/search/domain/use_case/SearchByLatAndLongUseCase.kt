package com.example.transweather.features.search.domain.use_case

import com.example.transweather.features.search.domain.repository.LocationByLatAndLongRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchByLatAndLongUseCase @Inject constructor(private val repo: LocationByLatAndLongRepository) {

    operator fun invoke(lat: String, lon: String) = flow {
        emit(repo.getLocationByLatAndLong(mapOf("lat" to lat, "lon" to lon)))
    }
}