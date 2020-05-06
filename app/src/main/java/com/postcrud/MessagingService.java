package com.postcrud;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> map = remoteMessage.getData();
        NotificationCompat.Builder builder;
        if (map.get("Type").equals("changedTimetable")) {
            builder = new NotificationCompat.Builder(this, "Post")
                    .setSmallIcon(R.drawable.ic_question_answer_blue_24dp)
                    .setContentTitle("Новый пост!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        } else {
            Intent cancel = new Intent("com.postcrud.cancel");
            PendingIntent cancelIntent = PendingIntent.getBroadcast(this, 0, cancel, PendingIntent.FLAG_CANCEL_CURRENT);
            RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.item_notification);
            RemoteViews notificationLayoutExpanded = new RemoteViews(getPackageName(), R.layout.item_notification);

            builder = new NotificationCompat.Builder(this, "Post")
                    .setSmallIcon(R.drawable.ic_question_answer_blue_24dp)
                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                    .setCustomContentView(notificationLayout)
                    .setCustomBigContentView(notificationLayoutExpanded)
                    .setContentTitle("Ваш пост лайкнули")
                    .setAutoCancel(true)
                    .setContentIntent(cancelIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }
        createNotificationChannel();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("New Like", "New Like", importance);
        channel.setDescription("New Like");
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (notificationManager != null) notificationManager.createNotificationChannel(channel);

    }

    public class NotificationCancelReceiver extends BroadcastReceiver {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceive(Context context, Intent intent) {
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) notificationManager.cancelAll();
        }
    }

}