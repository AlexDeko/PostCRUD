package com.postcrud.core.api

import com.postcrud.feature.data.dto.MediaResponseDto
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MediaApi {

    @Multipart
    @POST("media")
    suspend fun setMediaPost(@Part file: MultipartBody.Part): MediaResponseDto

}