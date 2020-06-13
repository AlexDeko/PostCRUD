package com.tribune.feature.data.dto.firebase

import com.tribune.feature.data.model.firebase.TokenFirebaseModel

class TokenFirebaseResponse(
    val id: Long = 0,
    val userId: Long,
    val token: String = ""
)

fun TokenFirebaseResponse.toModel() = TokenFirebaseModel(
    id = id,
    userId = userId,
    token = token
)