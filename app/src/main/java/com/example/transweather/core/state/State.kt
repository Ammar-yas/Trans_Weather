package com.example.transweather.core.state


sealed class State<T> {
    var hasBeenHandled = false

    class Initial<T>: State<T>()
    class Loading<T> : State<T>()
    data class Success<T>(val data: T? = null) : State<T>()
    data class Error<T>(val message: String? = null, val exception: Exception? = null) : State<T>()

}