package com.app.data.base.util


sealed class ResultWrapper<out T> {
    data class Success<out T>(val data: T?) : ResultWrapper<T>()
    data class Error(val error: ErrorResponse) : ResultWrapper<Nothing>()
    class Loading<T>(data: T? = null) : ResultWrapper<T>()
}