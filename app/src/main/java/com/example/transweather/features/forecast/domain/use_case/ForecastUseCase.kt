package com.example.transweather.features.forecast.domain.use_case

import com.example.transweather.features.forecast.domain.repository.ForecastRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ForecastUseCase @Inject constructor(private val repo: ForecastRepository) {

    operator fun invoke(lat: String, lon: String) = flow {
        emit(repo.getForecast(mapOf("lat" to lat, "lon" to lon)))
    }
}