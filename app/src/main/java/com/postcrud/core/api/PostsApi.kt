package com.postcrud.core.api

import com.postcrud.feature.data.dto.PostResponseDto
import retrofit2.http.*

interface PostsApi {

    @GET("posts")
    suspend fun getPosts(): List<PostResponseDto>

    @GET("posts/{id}/{count}")
    suspend fun getPage(@Path("id") id: Long, @Path("count") count: Int): List<PostResponseDto>

    @GET("posts/last/{count}")
    suspend fun getLastPage(@Path("count") count: Int): List<PostResponseDto>

    @GET("posts/{id}")
    suspend fun getPostsById(@Path("id") id: Long): PostResponseDto


    @POST("posts")
    suspend fun createOrUpdatePost(@Body postResponseDto: PostResponseDto): PostResponseDto

    @POST("posts/{id}/likes")
    suspend fun setLikePost(@Path("id") id: Long): PostResponseDto

    @POST("posts/{id}/likes")
    suspend fun setDislikePost(@Path("id") id: Long): PostResponseDto

    @POST("posts/{id}/reposts")
    suspend fun createRepost(@Path("id") id: Long): PostResponseDto


    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") id: Long): PostResponseDto
}