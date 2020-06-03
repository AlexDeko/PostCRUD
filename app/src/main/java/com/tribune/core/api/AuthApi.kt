package com.tribune.core.api

import retrofit2.http.Body
import retrofit2.http.POST
import com.tribune.feature.data.dto.AuthenticationRequestDto
import com.tribune.feature.data.dto.AuthenticationResponseDto

interface AuthApi {

    @POST("registration")
    suspend fun signUp(@Body authenticationRequestDto: AuthenticationRequestDto): AuthenticationResponseDto

    @POST("authentication")
    suspend fun signIn(@Body authenticationRequestDto: AuthenticationRequestDto): AuthenticationResponseDto
}