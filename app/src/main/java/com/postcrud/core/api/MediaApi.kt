package com.postcrud.core.api

import com.postcrud.feature.data.dto.MediaResponseDto
import retrofit2.http.POST

interface MediaApi {

    @POST("media")
    fun setMediaPost(): MediaResponseDto

}