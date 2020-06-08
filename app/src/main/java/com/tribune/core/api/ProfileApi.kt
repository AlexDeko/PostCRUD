package com.tribune.core.api

import com.tribune.feature.data.dto.user.UserResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProfileApi {

    @GET("me")
    suspend fun getProfile(): UserResponseDto

    @POST("user/update")
    suspend fun updateUser(@Body user: UserResponseDto)
}