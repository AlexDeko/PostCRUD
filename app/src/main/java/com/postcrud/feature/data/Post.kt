package com.postcrud.feature.data

import com.postcrud.feature.data.model.PostType

data class Post(
    val id: Long,
    val ownerId: Long,
    val author: String,
    val created: Long,
    var content: String? = null,
    var countLike: Long = 0,
    var isLike: Boolean = false,
    var countRepost: Int = 0,
    val type: PostType = PostType.POST,
    val adsUrl: String? = null,
    var countViews: Long = 0,
    val parentId: Long? = null,
    val imageId: Long? = null,
    val videoUrl: String? = null,
    val countComment: Long = 0,
    val isCanCommented: Boolean = true,
    val location: String? = null
)