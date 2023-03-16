package com.example.transweather.features.forecast.data.model.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class ForecastDto(
    @SerializedName("list")
    val list: List<ForecastIntervalData>?,
    @SerializedName("city")
    val city: City?
)