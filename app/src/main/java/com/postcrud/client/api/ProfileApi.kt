package com.postcrud.client.api

import io.reactivex.Single
import retrofit2.http.GET
import com.postcrud.data.dto.user.UserResponseDto

interface ProfileApi {

    @GET("me")
    fun getProfile(): Single<UserResponseDto>
}