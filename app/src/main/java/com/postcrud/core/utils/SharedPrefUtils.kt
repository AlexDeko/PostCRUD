package com.postcrud.core.utils

import android.content.Context
import com.postcrud.API_SHARED_FILE
import com.postcrud.FIRST_TIME_SHARED_KEY
import com.postcrud.LAST_TIME_VISIT_SHARED_KEY

fun isFirstTimeWork(context: Context) =
    context.getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE).getLong(
        LAST_TIME_VISIT_SHARED_KEY, 0
    ) == 0L

fun setLastVisitTimeWork(context: Context, currentTimeMillis: Long) =
    context.getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE)
        .edit()
        .putLong(LAST_TIME_VISIT_SHARED_KEY, currentTimeMillis)
        .commit()