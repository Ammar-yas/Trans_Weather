package com.example.transweather.features.common.use_case

import com.example.transweather.core.storage_manager.StorageManager
import com.example.transweather.features.search.data.model.response.LocationResponseDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import javax.inject.Inject

private const val SEARCH_HISTORY = "SEARCH_HISTORY"

class SearchHistoryUseCase @Inject constructor(
    private val storageManager: StorageManager, gson: Gson
) {

    private val historyDeque: ArrayDeque<LocationResponseDto>

    init {
        val historyType: Type = object : TypeToken<ArrayDeque<LocationResponseDto>>() {}.type
        val searchHistoryJson = storageManager.getString(SEARCH_HISTORY, "[]")
        historyDeque = gson.fromJson(searchHistoryJson, historyType)
    }


    fun setLastSearchedLocation(location: LocationResponseDto) {
        historyDeque.remove(location)
        if (historyDeque.size == 10) historyDeque.removeLast()
        historyDeque.addFirst(location)
        saveSearchHistory()
    }

    fun getLastSearchedLocation() = historyDeque.firstOrNull()

    fun getLastSearchedList(size: Int = 10) = historyDeque.take(size)

    private fun saveSearchHistory() = storageManager.setObject(SEARCH_HISTORY, historyDeque)

}