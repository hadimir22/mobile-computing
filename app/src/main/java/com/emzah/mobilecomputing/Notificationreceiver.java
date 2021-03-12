package com.emzah.mobilecomputing;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class Notificationreceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 22;
    private static final String CHANNEL_ID = "notify";
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        // Build notification based on Intent

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);

        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, context.getString(R.string.notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setAutoCancel(true)
                .setContentTitle("Mobile Computing")
                .setContentText("You have a reminder please check the App")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(""))
                .setSmallIcon(R.drawable.ic_baseline_add_24)
                .setLargeIcon(getLargeIcon(context))
                .setDefaults(Notification.DEFAULT_VIBRATE);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private Bitmap getLargeIcon(Context context) {
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_baseline_add_24);
    }
}