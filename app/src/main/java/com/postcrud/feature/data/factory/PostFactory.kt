package com.postcrud.feature.data.factory

import com.postcrud.feature.data.dto.PostResponseDto

class PostFactory {
    fun creteNewPost(contentText: String): PostResponseDto {
        return PostResponseDto(
            id = 0, ownerId = 0, author = "Вася", content = contentText)
    }
}