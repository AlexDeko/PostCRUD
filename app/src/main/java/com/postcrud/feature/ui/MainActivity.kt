package com.postcrud.feature.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.postcrud.R
import com.postcrud.SHOW_NOTIFICATION_AFTER_UNVISITED_MS
import com.postcrud.component.notification.NotificationHelper
import com.postcrud.component.notification.UserNotHereWorker
import com.postcrud.core.utils.isFirstTimeWork
import com.postcrud.core.utils.setLastVisitTimeWork
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleJob()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (isFirstTimeWork(this)) {
            NotificationHelper.comeBackNotification(this)
            setLastVisitTimeWork(this, System.currentTimeMillis())
        }else{
            setLastVisitTimeWork(this, System.currentTimeMillis())
        }
    }

    private fun scheduleJob() {
        val checkWork =
            PeriodicWorkRequestBuilder<UserNotHereWorker>(SHOW_NOTIFICATION_AFTER_UNVISITED_MS,
                TimeUnit.MILLISECONDS
            ).build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork("user_present_work",
                ExistingPeriodicWorkPolicy.KEEP, checkWork)
    }
}