package com.example.transweather.features.current_weather.domain.use_case

import com.example.transweather.core.state.State
import com.example.transweather.core.storage_manager.StorageManager
import com.example.transweather.features.current_weather.data.model.response.CurrentWeatherDto
import com.example.transweather.features.current_weather.domain.model.CurrentWeatherUIModel
import com.example.transweather.features.current_weather.domain.repository.CurrentWeatherRepository
import com.example.transweather.features.search.data.model.response.LocationResponseDto
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

//I can call API to fetch by Unit but will do calculations here to showcase how Can I use the use case in a real world scenario

class CurrentWeatherUseCase @Inject constructor(
    private val repo: CurrentWeatherRepository, private val storageManager: StorageManager
) {
    var measuringUnit: Unit

    init {
        val unitString = storageManager.getString(UNIT, Unit.Celsius.toString())
        measuringUnit = if (unitString == Unit.Celsius.toString()) Unit.Celsius else Unit.Fahrenheit
    }

    operator fun invoke(locationResponseDto: LocationResponseDto, unit: Unit? = null) = flow {
        if (unit != null) {
            measuringUnit = unit
            storageManager.setString(UNIT, Unit.Celsius.toString())
        }
        val response = repo.getCurrentWeather(locationResponseDto.queryMap)
        emit(getUiState(response, locationResponseDto))
    }

    private fun getUiState(
        state: State<CurrentWeatherDto>,
        locationResponseDto: LocationResponseDto
    ): State<CurrentWeatherUIModel> {
        return when (state) {
            is State.Error -> State.Error(state.message, state.exception)
            is State.Initial -> State.Initial()
            is State.Loading -> State.Loading()
            is State.Success -> State.Success(getUiModel(state.data, locationResponseDto))
        }

    }

    private fun getUiModel(data: CurrentWeatherDto?, locationResponseDto: LocationResponseDto): CurrentWeatherUIModel? {
        data ?: return null
        return CurrentWeatherUIModel(locationResponseDto).apply {
            iconUrl = "https://openweathermap.org/img/wn/${data.weather?.first()?.icon}@4x.png"
            temp = "${getTemp(data.main?.temp)}°"
            main = data.weather?.first()?.main ?: ""
            description = data.weather?.first()?.description ?: ""
            unit = measuringUnit
        }
    }

    private fun getTemp(kelvin: Double?): Int {
        kelvin ?: return 0
        return when (measuringUnit) {
            Unit.Celsius -> kelvin - 273.15
            Unit.Fahrenheit -> (kelvin - 273.15) * 1.8 + 32
        }.toInt()
    }

    companion object {
        private const val UNIT = "UNIT"
    }

    enum class Unit { Celsius, Fahrenheit }
}