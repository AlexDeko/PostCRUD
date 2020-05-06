package com.postcrud.data

import com.google.gson.annotations.SerializedName

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

data class Post(
    val id: Long,
    val author: String,
    val postType: PostType = PostType.POST,
    val text: String? = null,
    val date: String,
    val like: Like,
    val comment: Comment,
    val reply: Repost? = null,
    val address: String? = null,
    private val coordinates: Long? = null,
    val video: Video? = null,
    val adsUrl: String? = null,
    val countViews: Long = 0
) {

}