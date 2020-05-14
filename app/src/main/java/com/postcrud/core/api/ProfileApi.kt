package com.postcrud.core.api

import io.reactivex.Single
import retrofit2.http.GET
import com.postcrud.feature.data.dto.user.UserResponseDto

interface ProfileApi {

    @GET("me")
    fun getProfile(): Single<UserResponseDto>
}