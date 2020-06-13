package com.tribune.core.api

import com.tribune.feature.data.dto.firebase.TokenFirebaseResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface FirebaseApi {
    @POST("firebase/save")
    suspend fun saveToken(@Body token: TokenFirebaseResponse)
}