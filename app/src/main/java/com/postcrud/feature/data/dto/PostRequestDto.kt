package com.postcrud.feature.data.dto

data class PostRequestDto(
    val id: Long,
    val author: String,
    val content: String? = null
)