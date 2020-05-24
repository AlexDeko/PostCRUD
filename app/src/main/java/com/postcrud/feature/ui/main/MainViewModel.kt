package com.postcrud.feature.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.postcrud.PAGE_SIZE
import com.postcrud.component.lifecycle.MutableSingleLiveEvent
import com.postcrud.component.lifecycle.SingleLiveEvent
import com.postcrud.component.network.ErrorUiModel
import com.postcrud.component.network.NetworkError
import com.postcrud.component.network.NetworkErrorHandler
import com.postcrud.core.state.PagingState
import com.postcrud.core.state.StateUi
import com.postcrud.core.state.UiState
import com.postcrud.feature.data.dto.PostResponseDto
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

//toDo()
class MainViewModel : ViewModel() {


    private val _errorEvent = MutableSingleLiveEvent<NetworkError>()
    val errorEvent: SingleLiveEvent<NetworkError>
        get() = _errorEvent
    private val _uiModel = MutableLiveData<UiState>(UiState.Empty)
    val uiModel: LiveData<UiState>
        get() = _uiModel

    private var loadPageJob: Job? = null
    private var refreshJob: Job? = null

    private var currentState: PagingState<PostResponseDto> = Empty

    private var targetData: List<PostResponseDto> = emptyList()


    fun showNextPage() = currentState.showNextPage()

    fun refresh(): Unit = currentState.refresh()

    fun retry(): Unit = currentState.retry()


    private object Empty : PagingState<PostResponseDto>

    private inner class EmptyProgress : PagingState<PostResponseDto> {
        override fun refresh() {

        }

        override fun newData(data: List<PostResponseDto>) {
            if (data.isNotEmpty()) {
                currentState = if (data.size == PAGE_SIZE) Data() else AllData()
                targetData = data
                _uiModel.value = UiState.Data(data)
            } else {
                currentState = EmptyData()

                _uiModel.value = UiState.NotFound
            }
        }

        override fun fail(throwable: Throwable) {
            val error = NetworkErrorHandler.handle(throwable)

            currentState = EmptyError(error)

            _uiModel.value = UiState.Error(error)
        }
    }

    private inner class EmptyError(private val error: NetworkError) : PagingState<PostResponseDto> {

        override fun refresh() {
            currentState = EmptyProgress()

            _uiModel.value = UiState.Refreshing.Error(error)

            refreshLoading()
        }
    }

    private inner class EmptyData : PagingState<PostResponseDto> {

        override fun refresh() {
            currentState = EmptyProgress()

            _uiModel.value = UiState.Refreshing.Empty

            startLoading()
        }
    }

    private inner class Data : PagingState<PostResponseDto> {

        override fun refresh() {
            currentState = Refresh()

            _uiModel.value = UiState.Refreshing.Data(targetData)

            refreshLoading()
        }

        override fun showNextPage() {
            currentState = PageProgress()

            _uiModel.value = UiState.Data(targetData)

            startLoading()
        }

    }

    private inner class PageError(private val error: NetworkError) : PagingState<PostResponseDto> {

        override fun refresh() {
            currentState = Refresh()

            _uiModel.value = UiState.Refreshing.Data(targetData)

            refreshLoading()
        }

        override fun retry() {
            currentState = PageProgress()

            _uiModel.value = UiState.Data(targetData)

            startLoading()
        }
    }

    private inner class Refresh : PagingState<PostResponseDto> {

        override fun refresh() {
            startLoading()
        }

        override fun newData(data: List<PostResponseDto>) {
            targetData = data
            if (data.isNotEmpty()) {
                currentState = if (data.size == PAGE_SIZE) Data() else AllData()
                targetData = data
                _uiModel.value = UiState.Data(data)
            } else {
                currentState = EmptyData()

                _uiModel.value = UiState.NotFound
            }
        }

        override fun fail(throwable: Throwable) {
            currentState = Data()

            _uiModel.value = UiState.Data(targetData)

            _errorEvent.sendEvent(NetworkErrorHandler.handle(throwable))
        }

    }

    private inner class PageProgress : PagingState<PostResponseDto> {

        override fun newData(data: List<PostResponseDto>) {
            targetData = targetData + data
            if (data.isNotEmpty()) {
                currentState = if (data.size == PAGE_SIZE) Data() else AllData()
                _uiModel.value = UiState.Data(targetData)
            } else {
                currentState = EmptyData()

                _uiModel.value = UiState.NotFound
            }
        }

        override fun refresh() {
            currentState = Refresh()

            _uiModel.value = UiState.Refreshing.Data(targetData)

            refreshLoading()
        }

        override fun fail(throwable: Throwable) {
            val error = NetworkErrorHandler.handle(throwable)

            currentState = PageError(error)

            _uiModel.value = UiState.Data(targetData)
        }

    }

    private inner class AllData : PagingState<PostResponseDto> {

        override fun refresh() {
            currentState = Refresh()

            _uiModel.value = UiState.Refreshing.Data(targetData)

            refreshLoading()
        }
    }

    private fun startLoading() {
        loadPageJob?.cancel()
        loadPageJob = viewModelScope.launch {
            try {
                val page = targetData.size / PAGE_SIZE + 1

                //    val nextPage = search.invoke(query, PAGE_SIZE, page).map(::RepositoryUiModel)

                //     currentState.newData(nextPage)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                fail(exception)
            }
        }
    }

    private fun refreshLoading() {
        refreshJob?.cancel()
        loadPageJob?.cancel()
        refreshJob = viewModelScope.launch {
            try {
                //    val newPage = search.invoke(query, PAGE_SIZE, 1).map(::PostResponseDto)

                targetData = emptyList()
                //  currentState.newData(newPage)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                fail(exception)
            }
        }
    }

    private fun fail(throwable: Throwable) {
        currentState.fail(throwable)
    }

}