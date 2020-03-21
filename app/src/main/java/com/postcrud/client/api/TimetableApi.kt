package com.postcrud.client.api

import io.reactivex.Single
import retrofit2.http.GET
import com.postcrud.data.dto.schdule.ScheduleDayDto

interface TimetableApi {

    @GET("schedule")
    fun getTimetableList(): Single<List<ScheduleDayDto>>
}