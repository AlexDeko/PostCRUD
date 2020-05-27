package com.postcrud.component.notification

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.postcrud.R
import com.postcrud.feature.data.dto.MediaResponseDto
import kotlin.random.Random


object NotificationHelper {
    private val UPLOAD_CHANEL_ID = "upload_chanel_id"
    private var channelCreated = false
    private var lastNotificationId: Int? = null

    private fun createNotificationChannel(context: Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Media uploading"
            val descriptionText = "Notifies when media upload during post creation"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(UPLOAD_CHANEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun comeBackNotification(context: Context) {
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            createBuilder(
                context,
                "Понравилось ли вам у нас?",
                "Дорогой пользователь, возвращайтесь к нам скорее",
                NotificationManager.IMPORTANCE_HIGH
            )
        } else {
            createBuilder(
                context,
                "Понравилось ли вам у нас?",
                "Дорогой пользователь, возвращайтесь к нам скорее"
            )
        }
        showNotification(context, builder)

    }

    fun mediaUploaded(type: MediaResponseDto, context: Context) {
        createNotificationChannelIfNotCreated(context)
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            createBuilder(
                context,
                "Media uploaded",
                "your ${type.id.toLowerCase()} successfully uploaded.",
                NotificationManager.IMPORTANCE_HIGH
            )
        } else {
            createBuilder(
                context,
                "Media uploaded",
                "your ${type.id.toLowerCase()} successfully uploaded."
            )
        }
        showNotification(context, builder)
    }

    private fun showNotification(
        context: Context,
        builder: NotificationCompat.Builder
    ) {
        with(NotificationManagerCompat.from(context)) {
            val notificationId = Random.nextInt(100000)
            lastNotificationId = notificationId
            notify(notificationId, builder.build())
        }
    }

    @TargetApi(24)
    private fun createBuilder(
        context: Context,
        title: String,
        content: String,
        priority: Int
    ): NotificationCompat.Builder {
        val builder = createBuilder(context, title, content)
        builder.priority = priority
        return builder
    }

    private fun createBuilder(
        context: Context,
        title: String,
        content: String
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, UPLOAD_CHANEL_ID)
            .setSmallIcon(R.drawable.ic_question_answer_blue_24dp)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }

    private fun createNotificationChannelIfNotCreated(context: Context) {
        if (!channelCreated) {
            createNotificationChannel(context)
            channelCreated = true
        }
    }
}