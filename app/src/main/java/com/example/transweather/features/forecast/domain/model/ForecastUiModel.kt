package com.example.transweather.features.forecast.domain.model

import com.example.transweather.features.common.convertKelvinToUnit
import com.example.transweather.features.common.models.Unit
import com.example.transweather.features.forecast.data.model.response.ForecastIntervalData
import com.example.transweather.features.search.data.model.response.LocationResponseDto
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ForecastUiModel(
    intervals: List<ForecastIntervalData>, val location: LocationResponseDto, unit: Unit
) {
    private val dateFormat = SimpleDateFormat("dd MMM HH:mm", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    val name = location.name
    val tempIntervals = intervals.map {
        TempIntervalUiModel(
            dayTime = dateFormat.format(TimeUnit.SECONDS.toMillis(it.dt)),
            temperature = "${convertKelvinToUnit(it.main?.temp, unit).toInt()}°",
            iconUrl = "https://openweathermap.org/img/wn/${it.weather?.first()?.icon}@2x.png"
        )
    }
}

data class TempIntervalUiModel(
    val dayTime: String,
    val temperature: String,
    val iconUrl: String
)
