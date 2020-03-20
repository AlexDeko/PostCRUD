package com.postcrud.data

data class Comment(
    val count: Long,
    val canPost: Boolean = false
) {
}