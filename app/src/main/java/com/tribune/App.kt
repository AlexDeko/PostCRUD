package com.tribune

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.tribune.core.api.*
import com.tribune.core.utils.getString
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)

            modules(
                listOf(
                    sharedPreferencesModule,
                    networkModule
                )
            )
        }
    }
}

val networkModule = module {
    single {

        OkHttpClient.Builder()
            .followSslRedirects(true)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(
                object : Interceptor {
                    override fun intercept(chain: Interceptor.Chain): Response {
                        val original = chain.request()
                        val token = get<SharedPreferences>().getString(PREFS_TOKEN)
                        return if (token != null) {
                            val requestBuilder = original.newBuilder()
                                .addHeader("Authorization", "Bearer $token")
                            val request = requestBuilder.build()
                            chain.proceed(request)
                        } else {
                            chain.proceed(original)
                        }
                    }
                }
            )
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://server-post.herokuapp.com/api/v1/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(get<OkHttpClient>())
            .build()
    }

    single { get<Retrofit>().create(AuthApi::class.java) }
    single { get<Retrofit>().create(MediaApi::class.java) }
    single { get<Retrofit>().create(PostsApi::class.java) }
    single { get<Retrofit>().create(ProfileApi::class.java) }
    single { get<Retrofit>().create(FirebaseApi::class.java) }

}

val sharedPreferencesModule = module {
    single { androidContext().getSharedPreferences("settings", Context.MODE_PRIVATE) }
}

const val PREFS_TOKEN = "token"
const val PREFS_TOKEN_FIREBASE = "token_firebase"
const val PAGE_SIZE = 20
const val API_SHARED_FILE = "API_shared_file"
const val FIRST_TIME_SHARED_KEY = "first_time_shared_key"
const val LAST_TIME_VISIT_SHARED_KEY = "last_time_visit_shared_key"
const val SHOW_NOTIFICATION_AFTER_UNVISITED_MS = 10L * 60L * 1000L
const val ARG_TOKEN_FIREBASE = "tokenFirebase"
const val ARG_IMAGE_ID = "imageId"
const val ARG_POST_ID = "postId"
const val NEW_LIKE_FCM_ID = "New Like"
const val NEW_POST_FCM_ID = "New Post"
const val UNAUTHORIZED_HTTP_STATUS_CODE = "HTTP 401 Unauthorized"