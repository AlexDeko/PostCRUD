package com.postcrud.core.api

import retrofit2.http.GET
import com.postcrud.feature.data.dto.PostResponseDto
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface PostsApi {

    @GET("posts")
    fun getPosts(): List<PostResponseDto>

    @GET("posts/{id}")
    fun getPostsById(@Path("id") id: Long): PostResponseDto


    @POST("posts")
    fun setPost(): PostResponseDto

    @POST("posts/{id}/likes")
    fun setLikePost(@Path("id") id: Long): PostResponseDto

    @POST("posts/{id}/likes")
    fun setDislikePost(@Path("id") id: Long): PostResponseDto

    @POST("posts/{id}/reposts")
    fun createRepost(@Path("id") id: Long): PostResponseDto


    @DELETE("posts/{id}")
    fun deletePost(@Path("id") id: Long): PostResponseDto
}