package com.example.transweather.features.current_weather.data.model.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Sys(
    @SerializedName("type")
    val type: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("sunrise")
    val sunrise: Int?,
    @SerializedName("sunset")
    val sunset: Int?
)