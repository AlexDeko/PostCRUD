package com.postcrud.data.model

import com.google.gson.annotations.SerializedName

data class PostModel(
    val id: Long,
    val author: String,
    val content: String? = null,
    val created: Int = (System.currentTimeMillis() / 1000).toInt(),
    val likes: Int = 0,
    val postType: PostType = PostType.POST
)

enum class PostType {
    @SerializedName("PostType.POST")
    POST,
    @SerializedName("PostType.REPOST")
    REPOST,
    @SerializedName("PostType.ADS")
    ADS,
    @SerializedName("PostType.VIDEO")
    VIDEO,
    @SerializedName("PostType.EVENT")
    EVENT
}
