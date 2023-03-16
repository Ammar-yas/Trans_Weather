package com.example.transweather.features.forecast.di

import com.example.transweather.features.common.services.OpenWeatherService
import com.example.transweather.features.forecast.data.repository.ForecastRepositoryImpl
import com.example.transweather.features.forecast.domain.repository.ForecastRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class ForecastModule {

    @Provides
    fun provideForecastRepositoryRepository(service: OpenWeatherService): ForecastRepository =
        ForecastRepositoryImpl(service)
}