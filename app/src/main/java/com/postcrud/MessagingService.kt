package com.postcrud

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessagingService : FirebaseMessagingService() {


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val map = remoteMessage.data
        val builder: NotificationCompat.Builder

        builder = if (map["Type"] == "changedTimetable") {

            NotificationCompat.Builder(this, "Post")
                .setSmallIcon(R.drawable.ic_question_answer_blue_24dp)
                .setContentTitle("Новый пост!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        } else {
            val cancel = Intent("com.postcrud.cancel")
            val cancelIntent =
                PendingIntent.getBroadcast(this, 0, cancel, PendingIntent.FLAG_CANCEL_CURRENT)
            val notificationLayout =
                RemoteViews(packageName, R.layout.item_notification)
            val notificationLayoutExpanded =
                RemoteViews(packageName, R.layout.item_notification)

            NotificationCompat.Builder(this, "Post")
                .setSmallIcon(R.drawable.ic_question_answer_blue_24dp)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayout)
                .setCustomBigContentView(notificationLayoutExpanded)
                .setContentTitle("Ваш пост лайкнули")
                .setAutoCancel(true)
                .setContentIntent(cancelIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(1, builder.build())
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("New Like", "New Like", importance)
        channel.description = "New Like"
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        val notificationManager = getSystemService(
            NotificationManager::class.java
        )
        notificationManager?.createNotificationChannel(channel)
    }
}