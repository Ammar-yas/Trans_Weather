package com.example.transweather.features.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.transweather.core.state.State
import com.example.transweather.features.common.models.Unit
import com.example.transweather.features.common.use_case.SearchHistoryUseCase
import com.example.transweather.features.current_weather.domain.model.CurrentWeatherUIModel
import com.example.transweather.features.current_weather.domain.use_case.CurrentWeatherUseCase
import com.example.transweather.features.forecast.domain.model.ForecastUiModel
import com.example.transweather.features.forecast.domain.use_case.ForecastUseCase
import com.example.transweather.features.search.data.model.response.LocationResponseDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val searchHistoryUseCase: SearchHistoryUseCase,
    private val forecastUseCase: ForecastUseCase,
    private val currentWeatherUseCase: CurrentWeatherUseCase
) : ViewModel() {


    private val _currentWeatherState =
        MutableStateFlow<State<CurrentWeatherUIModel>>(State.Initial())
    val currentWeatherState: StateFlow<State<CurrentWeatherUIModel>> = _currentWeatherState

   private val _forecastState =
        MutableStateFlow<State<ForecastUiModel>>(State.Initial())
    val forecastState: StateFlow<State<ForecastUiModel>> = _forecastState


    fun setSearchLocation(location: LocationResponseDto) {
        searchHistoryUseCase.setLastSearchedLocation(location)
    }

    fun getSearchHistory(size: Int) = searchHistoryUseCase.getLastSearchedList(size)

    fun getCurrentWeather() {
        val lastSearchedLocation = searchHistoryUseCase.getLastSearchedLocation()
        if (lastSearchedLocation == null) {
            viewModelScope.launch {
                _currentWeatherState.emit(State.Success(null))
            }
            return
        }
        getWeatherForLocation(lastSearchedLocation)

    }

    fun getWeatherForLocation(
        location: LocationResponseDto, unit: Unit? = null
    ) {
        viewModelScope.launch {
            _currentWeatherState.emit(State.Loading())
            currentWeatherUseCase(location, unit).collectLatest {
                _currentWeatherState.emit(it)
            }
        }
    }

    fun getCurrentForecast() {
        val lastSearchedLocation = searchHistoryUseCase.getLastSearchedLocation()
        if (lastSearchedLocation == null) {
            viewModelScope.launch {
                _forecastState.emit(State.Success(null))
            }
            return
        }
        getForecastForLocation(lastSearchedLocation)

    }

    fun getForecastForLocation(location: LocationResponseDto, onlyOneDay: Boolean? = null) {
        viewModelScope.launch {
            _forecastState.emit(State.Loading())
            forecastUseCase(location, onlyOneDay).collectLatest {
                _forecastState.emit(it)
            }
        }
    }

}