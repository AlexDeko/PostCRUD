package com.tribune.feature.data.dto

import android.graphics.Bitmap
import com.tribune.feature.data.model.PostModel
import com.tribune.feature.data.model.PostType

data class PostResponseDto(
    val id: Long,
    val ownerId: Long,
    val author: String,
    val createdDate: Long,
    var content: String = "",
    var countLike: Long = 0,
    var isLike: Boolean = false,
    var countRepost: Long = 0,
    val type: PostType = PostType.POST,
    val adsUrl: String? = null,
    var countViews: Long = 0,
    val parentId: Long? = null,
    val imageId: String? = null,
    val videoUrl: String? = null,
    val countComment: Long = 0,
    val isCanCommented: Boolean = true,
    val selectedLocation: String? = null,
    val firebaseId: String? = null
) {



    companion object {
        fun PostResponseDto.toModel() = PostModel(
            id = this.id,
            ownerId = this.ownerId,
            author = this.author,
            createdDate = this.createdDate,
            content = this.content,
            countLike = this.countLike,
            isLike = this.isLike,
            countRepost = this.countRepost,
            type = this.type,
            adsUrl = this.adsUrl,
            countViews = this.countViews,
            parentId = this.parentId,
            imageId = this.imageId,
            videoUrl = this.videoUrl,
            countComment = this.countComment,
            isCanCommented = this.isCanCommented,
            selectedLocation = this.selectedLocation
        )

        fun PostModel.toDto() = PostResponseDto(
            id = this.id,
            ownerId = this.ownerId,
            author = this.author,
            createdDate = this.createdDate,
            content = this.content,
            countLike = this.countLike,
            isLike = this.isLike,
            countRepost = this.countRepost,
            type = this.type,
            adsUrl = this.adsUrl,
            countViews = this.countViews,
            parentId = this.parentId,
            imageId = this.imageId,
            videoUrl = this.videoUrl,
            countComment = this.countComment,
            isCanCommented = this.isCanCommented,
            selectedLocation = this.selectedLocation
        )


    }


}
