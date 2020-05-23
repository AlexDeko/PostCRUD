package com.postcrud.component.network

object NetworkErrorHandler {

    fun handle(throwable: Throwable): NetworkError =
        when (throwable) {
            is TooManyRequestsError -> NetworkError.TooManyRequests
            else -> NetworkError.NoInternet
        }
}