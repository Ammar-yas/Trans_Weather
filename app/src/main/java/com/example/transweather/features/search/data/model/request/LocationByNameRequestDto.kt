package com.example.transweather.features.search.data.model.request

data class LocationByNameRequestDto(
    val name: String, val limit: Int = 5
) {
    val queryMap get() = mapOf("q" to name, "limit" to "$limit")
}