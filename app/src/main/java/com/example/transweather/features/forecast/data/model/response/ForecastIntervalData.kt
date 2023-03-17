package com.example.transweather.features.forecast.data.model.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class ForecastIntervalData(
    @SerializedName("dt")
    val dt: Long,
    @SerializedName("main")
    val main: Main?,
    @SerializedName("weather")
    val weather: List<Weather?>?
)