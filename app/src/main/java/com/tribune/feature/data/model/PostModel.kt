package com.tribune.feature.data.model

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName
import com.tribune.feature.data.dto.PostResponseDto

data class PostModel(
    val id: Long,
    val ownerId: Long,
    val author: String,
    val createdDate: Long,
    var content: String = "",
    var isApprove: Boolean = false,
    val count_approve: Long = 0,
    var isNotApprove: Boolean = false,
    val count_not_approve: Long = 0,
    val countRepost: Long = 0,
    val type: PostType = PostType.POST,
    val urlLink: String? = null,
    var countViews: Long = 0,
    val parentId: Long? = null,
    val imageId: String?= null,
    var repost: PostResponseDto? = null
)

enum class PostType {
    @SerializedName("POST")
    POST,
    @SerializedName("REPOST")
    REPOST
}
