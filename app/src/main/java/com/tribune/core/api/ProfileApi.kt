package com.tribune.core.api

import com.tribune.feature.data.dto.user.UserResponseDto
import retrofit2.http.GET

interface ProfileApi {

    @GET("me")
    suspend fun getProfile(): UserResponseDto
}