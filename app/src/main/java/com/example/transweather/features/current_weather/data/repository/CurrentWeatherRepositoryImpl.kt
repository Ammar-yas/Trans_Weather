package com.example.transweather.features.current_weather.data.repository

import com.example.transweather.core.repository.BaseRepository
import com.example.transweather.core.state.State
import com.example.transweather.features.common.services.OpenWeatherService
import com.example.transweather.features.current_weather.data.model.response.CachedCurrentWeather
import com.example.transweather.features.current_weather.data.model.response.Coord
import com.example.transweather.features.current_weather.data.model.response.CurrentWeatherDto
import com.example.transweather.features.current_weather.domain.repository.CurrentWeatherRepository
import javax.inject.Inject

class CurrentWeatherRepositoryImpl @Inject constructor(private val service: OpenWeatherService) :
    BaseRepository<Map<String, String>, CurrentWeatherDto>(), CurrentWeatherRepository {

    private val cachedStates = mutableMapOf<Coord, CachedCurrentWeather>()

    override suspend fun getCurrentWeather(requestDto: Map<String, String>): State<CurrentWeatherDto> {
        val coord = Coord(lon = requestDto["lon"]?.toDouble(), lat = requestDto["lat"]?.toDouble())
        return if (cachedStates[coord]?.isValid == true) {
            cachedStates[coord]?.state!!
        } else {
            getApiCallState(requestDto)
        }
    }

    override suspend fun performApiCall(requestDto: Map<String, String>): State<CurrentWeatherDto> {
        val response = service.getWeather(requestDto)
        return handleResponse(response)
    }


}