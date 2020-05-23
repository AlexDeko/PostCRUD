package com.postcrud.core.state

import com.postcrud.component.network.NetworkError

interface StateUi<T> {

    fun empty() = Unit
    fun notFound() = Unit
    fun retry() = Unit
    fun showNextPage() = Unit
    fun newData(data: List<T>) = Unit
    fun error(error: NetworkError) = Unit
    fun refresh() {
        fun newData(data: List<T>) = Unit
        fun empty() = Unit
        fun error(error: NetworkError) = Unit
    }
}