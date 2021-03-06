package com.tribune.core.state

import com.tribune.component.network.NetworkError
import com.tribune.feature.data.dto.PostResponseDto

sealed class UiState {

    object Empty : UiState()
    object NotFound : UiState()
    data class Data(val items: List<PostResponseDto>) : UiState()
    data class Error(val error: NetworkError) : UiState()
    object EmptyProgress : UiState()
    sealed class Refreshing : UiState() {

        data class Data(val items: List<PostResponseDto>) : Refreshing()
        object Empty : Refreshing()
        data class Error(val error: NetworkError) : Refreshing()
    }
}