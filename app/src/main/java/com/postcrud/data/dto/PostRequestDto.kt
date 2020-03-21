package com.postcrud.data.dto

data class PostRequestDto(
    val id: Long,
    val author: String,
    val content: String? = null
)