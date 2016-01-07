package us.chary.pulsenotify;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class NotificationListener extends NotificationListenerService {

    private static Pulse pulse;
    private ArrayList<String> activeNotifs = new ArrayList<>();

    public static Pulse getPulseInstance(){
        if(pulse != null){
            pulse = new Pulse();
        }

        return pulse;
    }

    public NotificationListener() {
        getPulseInstance();
        pulse.connect((Activity) getApplication().getApplicationContext());
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
//        activeNotifs.remove(sbn.getPackageName());
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.d("WATCH", sbn.getPackageName());
        String pkgName = sbn.getPackageName();
        int color = sbn.getNotification().color;

        if(pkgName.equalsIgnoreCase("com.facebook.orca")){
            pulse.pushColor(color);
            activeNotifs.add(pkgName);
            Log.d("WATCH", "facebook thing. Color is " + sbn.getNotification().color);
        }else if(pkgName.equalsIgnoreCase("com.google.android.gm")){
            pulse.pushColor(color);
            activeNotifs.add(pkgName);
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

    public static void registerCalendarNotifierListenerOnBtn(final Activity activity) {
        activity.findViewById(R.id.calendar_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long f = new CalendarHelper().getTimeBetweenNowAndNextEvent(activity);
                if(f < 27000000){
                    long actual = (System.currentTimeMillis() / f) * 100;
                    getPulseInstance();
                }
            }
        });
    }
}