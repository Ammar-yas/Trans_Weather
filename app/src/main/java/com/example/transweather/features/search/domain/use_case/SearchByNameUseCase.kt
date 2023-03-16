package com.example.transweather.features.search.domain.use_case

import com.example.transweather.features.search.data.model.request.LocationByNameRequestDto
import com.example.transweather.features.search.domain.repository.LocationByNameRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchByNameUseCase @Inject constructor(private val repo: LocationByNameRepository) {


    operator fun invoke(name: String) = flow {
        emit(repo.getLocationByName(LocationByNameRequestDto(name)))
    }
}