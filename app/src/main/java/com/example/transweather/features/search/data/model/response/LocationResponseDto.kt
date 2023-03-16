package com.example.transweather.features.search.data.model.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class LocationResponseDto(
    @SerializedName("name")
    val name: String?,
    @SerializedName("lat")
    val lat: Double?,
    @SerializedName("lon")
    val lon: Double?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("state")
    val state: String?
){
    val imageUrl get() =  "https://source.unsplash.com/400x400/?$name"

    val queryMap get () = mapOf("lat" to lat.toString(), "lon" to lon.toString())

}