package com.example.transweather.features.forecast.data.model.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Sys(
    @SerializedName("pod")
    val pod: String?
)