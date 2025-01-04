package com.example.application2.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

public class AppNotificationManager {

    private static final String CHANNEL_ID = "default_channel";
    private static final String CHANNEL_NAME = "Default Channel";
    private static final String CHANNEL_DESCRIPTION = "Notifications";
    private static final String TAG = "AppNotificationManager";

    private final Context context;

    public AppNotificationManager(Context context) {
        this.context = context;
        createNotificationChannel();
    }

    /**
     * Creates a notification channel for Android O and higher.
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription(CHANNEL_DESCRIPTION);

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
                Log.d(TAG, "Notification channel created successfully.");
            } else {
                Log.e(TAG, "NotificationManager is null. Channel creation failed.");
            }
        } else {
            Log.d(TAG, "Notification channel not required for this Android version.");
        }
    }

    /**
     * Shows a notification.
     *
     * @param notificationId Unique ID for the notification.
     * @param title          The title of the notification.
     * @param message        The message of the notification.
     */
    public void showNotification(int notificationId, String title, String message) {
        // Check notification permission for Android 13 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "Notification permission is not granted.");
                Toast.makeText(context, "Please enable notification permissions in settings.", Toast.LENGTH_LONG).show();
                return;
            }
        }

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info) // Set a proper icon for notifications
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true); // Auto-dismiss when clicked

        // Get the notification manager and display the notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(notificationId, builder.build());
            Log.d(TAG, "Notification shown successfully. ID: " + notificationId);
        } else {
            Log.e(TAG, "NotificationManager is null. Unable to show notification.");
        }
    }
}
