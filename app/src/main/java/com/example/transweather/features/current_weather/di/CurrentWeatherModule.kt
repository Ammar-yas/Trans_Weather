package com.example.transweather.features.current_weather.di

import com.example.transweather.features.common.services.OpenWeatherService
import com.example.transweather.features.current_weather.data.repository.CurrentWeatherRepositoryImpl
import com.example.transweather.features.current_weather.domain.repository.CurrentWeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class CurrentWeatherModule {

    @Provides
    fun provideCurrentWeatherRepository(service: OpenWeatherService): CurrentWeatherRepository =
        CurrentWeatherRepositoryImpl(service)
}