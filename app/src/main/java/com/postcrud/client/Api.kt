package com.postcrud.client

import android.net.http.HttpResponseCache.install
import io.ktor.client.HttpClient
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.http.ContentType
import io.ktor.util.KtorExperimentalAPI

object Api {
    const val url =
        "https://raw.githubusercontent.com/AlexDeko/PostJson/master/src/main/java/json/Posts.json"
    const val urlAds =
        "https://raw.githubusercontent.com/AlexDeko/PostJson/master/src/main/java/json/PostsAds.json"


    @KtorExperimentalAPI
    val client = HttpClient {
        install(JsonFeature) {
            acceptContentTypes = mutableListOf(
                ContentType.Text.Plain,
                ContentType.Application.Json
            )
            serializer = GsonSerializer()
        }
    }
}