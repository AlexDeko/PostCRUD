package com.tribune.feature.data.dto

import com.tribune.feature.data.model.MediaModel
import com.tribune.feature.data.model.MediaType

data class MediaResponseDto(val id: String, val mediaType: MediaType) {
    companion object {
        fun fromModel(model: MediaModel) = MediaResponseDto(
            id = model.id,
            mediaType = model.mediaType
        )
    }
}