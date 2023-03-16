package com.example.transweather.features.search.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.transweather.core.state.State
import com.example.transweather.features.search.data.model.response.LocationResponseDto
import com.example.transweather.features.search.domain.use_case.GetLocationByZipUseCase
import com.example.transweather.features.search.domain.use_case.SearchByLatAndLongUseCase
import com.example.transweather.features.search.domain.use_case.SearchByNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchByNameUseCase: SearchByNameUseCase,
    private val getLocationByZipUseCase: GetLocationByZipUseCase,
    private val searchByLatAndLongUseCase: SearchByLatAndLongUseCase
) : ViewModel() {


    private val _searchState = MutableStateFlow<State<List<LocationResponseDto>>>(State.Initial())
    val searchState: StateFlow<State<List<LocationResponseDto>>> = _searchState


    fun searchLocationsByName(name: String) {
        viewModelScope.launch {
            _searchState.emit(State.Loading())
            searchByNameUseCase(name).collectLatest {
                _searchState.emit(it)
            }
        }
    }

    fun getLocationByZip(zip:String){
        viewModelScope.launch {
            getLocationByZipUseCase(zip).collectLatest {
                _searchState.emit(it)
            }
        }
    }

    fun searchLocationByLatAndLong(lat: String, lon: String){
        viewModelScope.launch {
            searchByLatAndLongUseCase(lat,lon).collectLatest {
                _searchState.emit(it)
            }
        }
    }
}