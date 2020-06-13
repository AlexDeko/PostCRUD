package com.tribune

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

class MessagingService : FirebaseMessagingService(), CoroutineScope by MainScope() {


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val map = remoteMessage.data
        val builder: NotificationCompat.Builder

        builder = if (map["title"] == NEW_POST_FCM_ID) {

            NotificationCompat.Builder(this, NEW_POST_FCM_ID)
                .setSmallIcon(R.drawable.ic_question_answer_blue_24dp)
                .setContentTitle(getString(R.string.message_title_new_post))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        } else {
            val args = Bundle()
            args.putLong("idPost", 0)

            val deeplink = NavDeepLinkBuilder(this)
                .setGraph(R.navigation.navigation_global)
                .setDestination(R.id.postFragment)
                .setArguments(args)
                .createPendingIntent()

            val notificationLayout =
                RemoteViews(packageName, R.layout.item_notification)
            val notificationLayoutExpanded =
                RemoteViews(packageName, R.layout.item_notification)

            NotificationCompat.Builder(this, NEW_LIKE_FCM_ID)
                .setSmallIcon(R.drawable.ic_question_answer_blue_24dp)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayout)
                .setCustomBigContentView(notificationLayoutExpanded)
                .setContentTitle(getString(R.string.message_title_new_approve))
                .setAutoCancel(true)
                .setContentIntent(deeplink)
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
        val channel = NotificationChannel(NEW_LIKE_FCM_ID, NEW_LIKE_FCM_ID, importance)
        channel.description = NEW_LIKE_FCM_ID
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        val notificationManager = getSystemService(
            NotificationManager::class.java
        )
        notificationManager?.createNotificationChannel(channel)
    }
}