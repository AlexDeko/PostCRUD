package com.postcrud.feature.data.factory

import com.postcrud.feature.data.dto.PostResponseDto
import com.postcrud.feature.data.dto.user.UserResponseDto


fun creteNewPost(contentText: String, ownerId: Long, author: String): PostResponseDto {
        return PostResponseDto(
            id = 0, ownerId = ownerId, author = author, content = contentText)
    }

