package com.postcrud.core.api

import retrofit2.http.Body
import retrofit2.http.POST
import com.postcrud.feature.data.dto.AuthenticationRequestDto
import com.postcrud.feature.data.dto.AuthenticationResponseDto

interface AuthApi {

    @POST("registration")
    suspend fun signUp(@Body authenticationRequestDto: AuthenticationRequestDto): AuthenticationResponseDto

    @POST("authentication")
    suspend fun signIn(@Body authenticationRequestDto: AuthenticationRequestDto): AuthenticationResponseDto
}