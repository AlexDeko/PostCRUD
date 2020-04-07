package com.postcrud.client.utils

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.postcrud.PREFS_TIMETABLE
import com.postcrud.data.dto.schdule.ScheduleDayDto

fun SharedPreferences.putString(key: String, value: String?) {
    edit().putString(key, value).apply()
}

fun SharedPreferences.getString(key: String) =
    getString(key, null)

fun SharedPreferences.putTimetableList(
    listValue: List<ScheduleDayDto>
) {
    val type = object : TypeToken<List<ScheduleDayDto>>() {}.type
    val jsonValue = Gson().toJson(listValue, type)
    edit().putString(PREFS_TIMETABLE, jsonValue).apply()
}

fun SharedPreferences.getTimetableList(): List<ScheduleDayDto> {
    val type = object : TypeToken<List<ScheduleDayDto>>() {}.type
    return Gson().fromJson(getString(PREFS_TIMETABLE, null), type)
}