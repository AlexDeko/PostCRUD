package com.postcrud.core.api

import com.postcrud.feature.data.dto.MediaResponseDto
import retrofit2.http.Multipart
import retrofit2.http.POST

interface MediaApi {

    @Multipart
    @POST("media")
    suspend fun setMediaPost(): MediaResponseDto

}