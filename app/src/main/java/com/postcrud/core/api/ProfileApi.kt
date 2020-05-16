package com.postcrud.core.api

import com.postcrud.feature.data.dto.user.UserResponseDto
import io.reactivex.Single
import retrofit2.http.GET

interface ProfileApi {

    @GET("me")
    fun getProfile(): Single<UserResponseDto>
}