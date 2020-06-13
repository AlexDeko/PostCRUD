package com.tribune.component.creator

import com.tribune.feature.data.dto.PostResponseDto
import java.util.*


fun creteNewPost(
    contentText: String,
    ownerId: Long,
    author: String,
    imageId: String? = null
): PostResponseDto {
    val time = Date().time
    return PostResponseDto(
        id = 0,
        ownerId = ownerId,
        author = author,
        content = contentText,
        imageId = imageId,
        createdDate = time
    )
}

