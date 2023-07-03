package etf.mr.project.activitymanager;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class AlarmBroadcastReceiver extends BroadcastReceiver {
    private static final String CHANEL_ID = "ma_chanel";

    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(intent.getStringExtra("content"), context);
    }

    private void showNotification(String content, Context context) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANEL_ID)
                .setSmallIcon(R.drawable.ic_notify)
                .setContentTitle("Activity Alarm!")
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Create an explicit intent for the notification's destination
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED)
            notificationManager.notify(1, builder.build());

    }
}
