package com.postcrud.core.api

import retrofit2.http.GET
import com.postcrud.feature.data.dto.PostResponseDto

interface NewsApi {
    @GET("posts")
    fun getPosts(): List<PostResponseDto>
}