package com.tribune.core.utils

import android.content.Context
import com.tribune.API_SHARED_FILE
import com.tribune.LAST_TIME_VISIT_SHARED_KEY
import com.tribune.SHOW_NOTIFICATION_AFTER_UNVISITED_MS

fun isFirstTimeWork(context: Context) =
    context.getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE).getLong(
        LAST_TIME_VISIT_SHARED_KEY, 0
    ) == 0L

fun setLastVisitTimeWork(context: Context, currentTimeMillis: Long) =
    context.getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE)
        .edit()
        .putLong(LAST_TIME_VISIT_SHARED_KEY, currentTimeMillis)
        .commit()

fun isShowNotifyWork(context: Context) =
    context.getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE).getLong(
        LAST_TIME_VISIT_SHARED_KEY, 0) < SHOW_NOTIFICATION_AFTER_UNVISITED_MS
