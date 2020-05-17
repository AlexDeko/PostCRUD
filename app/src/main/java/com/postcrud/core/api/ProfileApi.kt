package com.postcrud.core.api

import com.postcrud.feature.data.dto.user.UserResponseDto
import retrofit2.http.GET

interface ProfileApi {

    @GET("me")
    suspend fun getProfile(): UserResponseDto
}