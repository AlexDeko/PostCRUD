package com.postcrud.core.api

import retrofit2.http.GET
import com.postcrud.feature.data.dto.PostResponseDto
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface PostsApi {

    @GET("posts")
    suspend fun getPosts(): List<PostResponseDto>

    @GET("posts/{id}")
    suspend fun getPostsById(@Path("id") id: Long): PostResponseDto


    @POST("posts")
    suspend fun setPost(): PostResponseDto

    @POST("posts/{id}/likes")
    suspend fun setLikePost(@Path("id") id: Long): PostResponseDto

    @POST("posts/{id}/likes")
    suspend fun setDislikePost(@Path("id") id: Long): PostResponseDto

    @POST("posts/{id}/reposts")
    suspend fun createRepost(@Path("id") id: Long): PostResponseDto


    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") id: Long): PostResponseDto
}