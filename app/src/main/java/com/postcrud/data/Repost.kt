package com.postcrud.data

data class Repost(
    val count: Long,
    val text: String? = null,
    val author: String? = null,
    val date: String? = null
) {
}