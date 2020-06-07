package com.tribune.core.api

import com.tribune.feature.data.dto.PostResponseDto
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


    @POST("posts/save")
    suspend fun createPost(@Body postResponseDto: PostResponseDto, @Body tokenFireBase: String): PostResponseDto

    @POST("posts/update")
    suspend fun updatePost(@Body postResponseDto: PostResponseDto): PostResponseDto

    @POST("posts/{id}/approves")
    suspend fun setApprovePost(@Path("id") id: Long): PostResponseDto

    @POST("posts/{id}/not_approves")
    suspend fun setNotApprovePost(@Path("id") id: Long): PostResponseDto

    @POST("posts/{id}/unselected_approves")
    suspend fun setUnselectedApproves(@Path("id") id: Long): PostResponseDto

    @POST("posts/{id}/reposts")
    suspend fun createRepost(@Path("id") id: Long): PostResponseDto


    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") id: Long): PostResponseDto
}