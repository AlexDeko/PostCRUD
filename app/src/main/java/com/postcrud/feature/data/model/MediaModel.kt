package com.postcrud.feature.data.model

enum class MediaType {
    IMAGE
}

data class MediaModel(val id: String, val mediaType: MediaType)