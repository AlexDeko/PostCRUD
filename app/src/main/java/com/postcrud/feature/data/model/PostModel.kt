package com.postcrud.feature.data.model

import com.google.gson.annotations.SerializedName

data class PostModel(
    val id: Long,
    val ownerId: Long,
    val author: String,
    val createdDate: Long,
    var content: String? = null,
    var countLike: Long = 0,
    var isLike: Boolean = false,
    var countRepost: Long = 0,
    val type: PostType = PostType.POST,
    val adsUrl: String? = null,
    var countViews: Long = 0,
    val parentId: Long? = null,
    val imageId: Long? = null,
    val videoUrl: String? = null,
    val countComment: Long = 0,
    val isCanCommented: Boolean = true,
    val selectedLocation: String? = null
)

enum class PostType {
    @SerializedName("POST")
    POST,
    @SerializedName("REPOST")
    REPOST,
    @SerializedName("ADS")
    ADS,
    @SerializedName("VIDEO")
    VIDEO,
    @SerializedName("EVENT")
    EVENT
}
