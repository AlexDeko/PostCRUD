package com.postcrud.modelMy

import com.google.gson.annotations.SerializedName
import com.postcrud.data.Like
import com.postcrud.data.Repost
import com.postcrud.data.Video
import com.postcrud.data.Comment

enum class PostModelType {
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

data class PostModel(
    val id: Long,
    val author: String,
    val postType: PostModelType = PostModelType.POST,
    val source: PostModel? = null,
    val text: String? = null,
    val date: String,
    val like: Like,
    val comment: Comment,
    val reply: Repost? = null,
    val address: String? = null,
    val coordinates: Long? = null,
    val video: Video? = null,
    val adsUrl: String? = null
) {

}