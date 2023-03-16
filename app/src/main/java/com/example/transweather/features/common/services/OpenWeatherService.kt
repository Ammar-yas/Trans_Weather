package com.example.transweather.features.common.services

import com.example.transweather.features.current_weather.data.model.response.CurrentWeatherDto
import com.example.transweather.features.forecast.data.model.response.ForecastDto
import com.example.transweather.features.search.data.model.response.LocationResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface OpenWeatherService {

    @GET("/geo/1.0/direct")
    suspend fun getLocationByName(@QueryMap queryMap: Map<String, String>): Response<List<LocationResponseDto>>

    @GET("/geo/1.0/reverse")
    suspend fun getLocationByLatAndLong(@QueryMap queryMap: Map<String, String>): Response<List<LocationResponseDto>>

    @GET("/geo/1.0/zip")
    suspend fun getLocationByZip(@Query("zip") zipCode: String): Response<LocationResponseDto>
    @GET("/data/2.5/forecast")
    suspend fun getForecast(@QueryMap queryMap: Map<String, String>): Response<ForecastDto>

    @GET("/data/2.5/weather")
    suspend fun getWeather(@QueryMap queryMap: Map<String, String>): Response<CurrentWeatherDto>

}