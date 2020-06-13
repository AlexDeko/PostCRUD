package com.tribune.core.api

import com.tribune.feature.data.dto.AuthenticationRequestDto
import com.tribune.feature.data.dto.AuthenticationResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("registration")
    suspend fun signUp(@Body authenticationRequestDto: AuthenticationRequestDto): AuthenticationResponseDto

    @POST("authentication")
    suspend fun signIn(@Body authenticationRequestDto: AuthenticationRequestDto): AuthenticationResponseDto
}