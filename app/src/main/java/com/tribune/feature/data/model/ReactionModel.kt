package com.tribune.feature.data.model

data class ReactionModel(
    val id: Long,
    val postId: Long,
    val userId: Long,
    val createdDate: Long,
    val reactionType: ReactionType
)