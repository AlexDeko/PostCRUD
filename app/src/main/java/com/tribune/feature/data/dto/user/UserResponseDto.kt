package com.tribune.feature.data.dto.user

import com.tribune.feature.data.model.UserBadge

data class UserResponseDto(
    val id: Long,
    val username: String,
    val imageId: Long? = null,
    val badge: UserBadge? = null,
    val notApprove: Long = 0,
    val approve: Long = 0,
    val onlyReads: Boolean = false,
    val firebaseId: String? = null
)