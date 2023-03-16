package com.example.transweather.features.current_weather.data.model.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Wind(
    @SerializedName("speed")
    val speed: Double?,
    @SerializedName("deg")
    val deg: Int?,
    @SerializedName("gust")
    val gust: Double?
)