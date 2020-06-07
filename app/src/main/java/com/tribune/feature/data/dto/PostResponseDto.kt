package com.tribune.feature.data.dto

import android.graphics.Bitmap
import com.tribune.feature.data.model.PostModel
import com.tribune.feature.data.model.PostType

data class PostResponseDto(
    val id: Long,
    val ownerId: Long,
    val author: String,
    val createdDate: Long,
    val content: String = "",
    val isApprove: Boolean = false,
    val count_approve: Long = 0,
    val isNotApprove: Boolean = false,
    val count_not_approve: Long = 0,
    val countRepost: Long = 0,
    val type: PostType = PostType.POST,
    val urlLink: String? = null,
    val countViews: Long = 0,
    val parentId: Long? = null,
    val imageId: String?= null
) {



    companion object {
        fun PostResponseDto.toModel() = PostModel(
            id = this.id,
            ownerId = this.ownerId,
            author = this.author,
            createdDate = this.createdDate,
            content = this.content,
            isApprove = this.isApprove,
            count_approve = this.count_approve,
            isNotApprove = this.isNotApprove,
            count_not_approve = this.count_not_approve,
            countRepost = this.countRepost,
            type = this.type,
            urlLink = this.urlLink,
            countViews = this.countViews,
            parentId = this.parentId,
            imageId = this.imageId
        )

        fun PostModel.toDto() = PostResponseDto(
            id = this.id,
            ownerId = this.ownerId,
            author = this.author,
            createdDate = this.createdDate,
            content = this.content,
            isApprove = this.isApprove,
            count_approve = this.count_approve,
            isNotApprove = this.isNotApprove,
            count_not_approve = this.count_not_approve,
            countRepost = this.countRepost,
            type = this.type,
            urlLink = this.urlLink,
            countViews = this.countViews,
            parentId = this.parentId,
            imageId = this.imageId
        )


    }


}
