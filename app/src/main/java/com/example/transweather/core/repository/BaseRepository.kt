package com.example.transweather.core.repository

import com.example.transweather.core.state.State
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response

abstract class BaseRepository<RequestDto, ResponseDto>(
    protected var dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun getApiCallState(requestDto: RequestDto): State<ResponseDto> {
        return withContext(dispatcher) {
            try {
                performApiCall(requestDto)
            } catch (e: Exception) {
                State.Error(exception = e)
            }
        }
    }

    abstract suspend fun performApiCall(requestDto: RequestDto): State<ResponseDto>

    fun handleResponse(response: Response<ResponseDto>): State<ResponseDto> {
        return if (response.isSuccessful) {
            State.Success(response.body())
        } else {
            State.Error(exception = HttpException(response))
        }
    }

}