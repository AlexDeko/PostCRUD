package com.tribune.component.notification

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.tribune.core.utils.isShowNotifyWork

class UserNotHereWorker(val context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    override fun doWork(): Result {
        // если пользователь заходил меньше SHOW_NOTIFICATION_AFTER_UNVISITED_MS времени, то показываем нотификацию
        if (isShowNotifyWork(context = context)) {
            NotificationHelper.comeBackNotification(context)
            return Result.success()
        }
        return Result.retry()
    }
}
