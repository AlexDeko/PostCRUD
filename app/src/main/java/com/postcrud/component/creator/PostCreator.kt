package com.postcrud.component.creator

import com.postcrud.feature.data.dto.PostResponseDto


fun creteNewPost(contentText: String, ownerId: Long, author: String, imageId: String? = null): PostResponseDto {
        return PostResponseDto(
            id = 0, ownerId = ownerId, author = author, content = contentText, imageId = imageId)
    }

