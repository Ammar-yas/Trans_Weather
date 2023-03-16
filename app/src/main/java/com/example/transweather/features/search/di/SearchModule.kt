package com.example.transweather.features.search.di

import com.example.transweather.features.common.services.OpenWeatherService
import com.example.transweather.features.search.data.repository.LocationByLatAndLongRepositoryImpl
import com.example.transweather.features.search.data.repository.LocationByNameRepositoryImpl
import com.example.transweather.features.search.data.repository.LocationByZipRepositoryImpl
import com.example.transweather.features.search.domain.repository.LocationByLatAndLongRepository
import com.example.transweather.features.search.domain.repository.LocationByNameRepository
import com.example.transweather.features.search.domain.repository.LocationByZipRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class SearchModule {

    @Provides
    fun provideLocationByNameRepository(service: OpenWeatherService): LocationByNameRepository =
        LocationByNameRepositoryImpl(service)

    @Provides
    fun provideLocationByLatAndLongRepository(service: OpenWeatherService): LocationByLatAndLongRepository =
        LocationByLatAndLongRepositoryImpl(service)

    @Provides
    fun provideLocationByZipRepository(service: OpenWeatherService): LocationByZipRepository =
        LocationByZipRepositoryImpl(service)
}