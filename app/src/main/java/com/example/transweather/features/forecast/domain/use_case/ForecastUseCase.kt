package com.example.transweather.features.forecast.domain.use_case

import com.example.transweather.core.state.State
import com.example.transweather.core.storage_manager.StorageManager
import com.example.transweather.features.common.models.UNIT
import com.example.transweather.features.common.models.Unit
import com.example.transweather.features.current_weather.data.model.response.CurrentWeatherDto
import com.example.transweather.features.current_weather.domain.model.CurrentWeatherUIModel
import com.example.transweather.features.current_weather.domain.use_case.CurrentWeatherUseCase
import com.example.transweather.features.forecast.data.model.response.ForecastDto
import com.example.transweather.features.forecast.domain.model.ForecastUiModel
import com.example.transweather.features.forecast.domain.repository.ForecastRepository
import com.example.transweather.features.search.data.model.response.LocationResponseDto
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ForecastUseCase @Inject constructor(
    private val repo: ForecastRepository,
    storageManager: StorageManager
) {

    private var measuringUnit: Unit

    init {
        val unitString = storageManager.getString(UNIT, Unit.Celsius.toString())
        measuringUnit = if (unitString == Unit.Celsius.toString()) Unit.Celsius else Unit.Fahrenheit
    }

    operator fun invoke(location: LocationResponseDto, onlyOneDay: Boolean? = null) = flow {
        emit(repo.getForecast(location.queryMap))
    }.map {
        when (it) {
            is State.Error -> State.Error(it.message, it.exception)
            is State.Initial -> State.Initial()
            is State.Loading -> State.Loading()
            is State.Success -> State.Success(getUiModel(it.data, location, onlyOneDay))
        }
    }

    private fun getUiModel(data: ForecastDto?, location: LocationResponseDto,onlyOneDay: Boolean? = null
    ): ForecastUiModel? {
        data ?: return null
        val beforeTime = TimeUnit.MILLISECONDS.toSeconds(Date().time) + when(onlyOneDay){
            true -> TimeUnit.DAYS.toSeconds(1)
            false -> TimeUnit.DAYS.toSeconds(2)
            null -> TimeUnit.DAYS.toSeconds(5)
        }
        val filteredData = data.list?.filter { it.dt <= beforeTime } ?:  return null
        return ForecastUiModel(filteredData, location, measuringUnit)
    }
}