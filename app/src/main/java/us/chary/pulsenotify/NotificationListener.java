package us.chary.pulsenotify;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;

public class NotificationListener extends NotificationListenerService {
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.d("WATCH", sbn.getPackageName());
        String pkgName = sbn.getPackageName();
        if(pkgName.equalsIgnoreCase("com.facebook.orca")){
            Log.d("WATCH", "facebook thing. Color is " + sbn.getNotification().color);
        }else if(pkgName.equalsIgnoreCase("com.google.android.gm")){
            Log.d("WATCH", "google thing. Color is " + sbn.getNotification().color);
        }
    }

    public static void registerNotifListenerOnBtn(final Activity activity){
        activity.findViewById(R.id.send_notif_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notification notification = new
                        NotificationCompat.Builder(activity)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Notification Title")
                        .setContentText("Notification Message")
                        .setTicker("Ticker Text")
                        .setAutoCancel(true)
                        .setNumber(1)
                        .build();

                ((NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE)).notify(500, notification);
            }
        });
    }
}