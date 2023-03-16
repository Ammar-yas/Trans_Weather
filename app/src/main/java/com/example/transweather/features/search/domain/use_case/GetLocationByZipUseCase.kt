package com.example.transweather.features.search.domain.use_case

import com.example.transweather.core.state.State
import com.example.transweather.features.search.data.model.response.LocationResponseDto
import com.example.transweather.features.search.domain.repository.LocationByZipRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetLocationByZipUseCase @Inject constructor(private val repo: LocationByZipRepository) {

    operator fun invoke(zip: String) = flow {
        val mappedState: State<List<LocationResponseDto>> =
            when (val state = repo.getLocationByZip(zip)) {
                is State.Error -> State.Error(state.message, state.exception)
                is State.Initial -> State.Initial()
                is State.Loading -> State.Loading()
                is State.Success -> {
                    if (state.data != null) {
                        State.Success(listOf(state.data))
                    } else {
                        State.Success(null)
                    }
                }
            }
        emit(mappedState)
    }
}