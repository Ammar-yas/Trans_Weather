package com.example.transweather.features.current_weather.data.model.response

import com.example.transweather.core.state.State
import java.util.Date
import java.util.concurrent.TimeUnit

class CachedCurrentWeather(val state: State.Success<CurrentWeatherDto>) {
    val requestTime: Long = Date().time
    val isValid: Boolean get() = Date().time - requestTime < TimeUnit.MINUTES.toMillis(5)
}


