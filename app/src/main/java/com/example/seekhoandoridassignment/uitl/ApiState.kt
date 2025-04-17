package com.example.seekhoandoridassignment.uitl

sealed class ApiState<out T> {
    data object Loading : ApiState<Nothing>()
    data class Success<out T>(val data: T) : ApiState<T>()
    data class Error(val message: String? = null, val throwable: Throwable? = null) : ApiState<Nothing>()
}
sealed class HomePageState<out T> {
    data object Loading : HomePageState<Nothing>()
    data class TopAnime<out T>(val data: T) : HomePageState<T>()
    data class Season<out T>(val data: T) : HomePageState<T>()
    data class Error(val message: String? = null, val throwable: Throwable? = null) : HomePageState<Nothing>()
}

