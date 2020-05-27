package com.postcrud.component.notification

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class UserNotHereWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    override fun doWork(): Result {
        // если пользователь заходил меньше SHOW_NOTIFICATION_AFTER_UNVISITED_MS времени, то показываем нотификацию
        return Result.success()
    }

}
