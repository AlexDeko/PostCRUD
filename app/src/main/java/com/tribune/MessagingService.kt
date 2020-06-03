package com.tribune

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.Navigation
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tribune.feature.ui.MainActivity

class MessagingService : FirebaseMessagingService() {


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val map = remoteMessage.data
        val builder: NotificationCompat.Builder

        builder = if (map["title"] == "NewPost") {

            NotificationCompat.Builder(this, "Post")
                .setSmallIcon(R.drawable.ic_question_answer_blue_24dp)
                .setContentTitle("Новый пост!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        } else {
            val args = Bundle()
            args.putLong("idPost", 0)

            val deeplink = Navigation.findNavController(MainActivity(), R.id.host_global)
                .createDeepLink()
                .setDestination(R.id.action_mainFragment_to_postFragment)
                .setArguments(args)
                .createPendingIntent()

            val notificationLayout =
                RemoteViews(packageName, R.layout.item_notification)
            val notificationLayoutExpanded =
                RemoteViews(packageName, R.layout.item_notification)

            NotificationCompat.Builder(this, "NewLike")
                .setSmallIcon(R.drawable.ic_question_answer_blue_24dp)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayout)
                .setCustomBigContentView(notificationLayoutExpanded)
                .setContentTitle("Ваш пост лайкнули")
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