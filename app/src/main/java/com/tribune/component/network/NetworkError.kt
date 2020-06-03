package com.tribune.component.network

sealed class NetworkError {
    object NoInternet : NetworkError()
    object TooManyRequests : NetworkError()
}