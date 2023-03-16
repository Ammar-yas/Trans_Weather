package com.example.transweather.features.current_weather.data.model.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Coord(
    @SerializedName("lon")
    val lon: Double?,
    @SerializedName("lat")
    val lat: Double?
)