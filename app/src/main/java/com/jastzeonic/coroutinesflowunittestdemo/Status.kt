package com.jastzeonic.coroutinesflowunittestdemo

sealed class Result<T> {
    data class Success<T>(val result: T) : Result<T>()
    class Loading<T> : Result<T>()
    data class Failed<T>(val reason: Exception) : Result<T>()

}