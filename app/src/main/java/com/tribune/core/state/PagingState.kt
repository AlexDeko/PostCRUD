package com.tribune.core.state

interface PagingState<T> {
    fun retry() = Unit
    fun refresh() = Unit
    fun showNextPage() = Unit
    fun newData(data: List<T>) = Unit
    fun fail(throwable: Throwable) = Unit
}