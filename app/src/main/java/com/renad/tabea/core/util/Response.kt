package com.renad.tabea.core.util

sealed class Response<out T> {
    class Success<T>(val data: T?) : Response<T>()
    object Loading : Response<Nothing>()
    class Failure(val message: String) : Response<Nothing>()
}
