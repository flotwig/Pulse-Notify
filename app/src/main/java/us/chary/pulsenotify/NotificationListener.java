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

    public static final int FACEBOOK_COLOR = 0x0011ff;
    public static final int GMAIL_COLOR = 0xff1100;
    public static final int HANGOUTS_COLOR = 0x11ff11;
    public static Pulse pulse = new Pulse();
    private ArrayList<String> activeNotifs = new ArrayList<>();


    public NotificationListener() {
        Log.d("WATCH", "instantiated");
//        pulse.connect((Activity) getApplication().getApplicationContext());
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        String pkgName = sbn.getPackageName();
        if(pkgName.equalsIgnoreCase("com.facebook.orca")){
            pulse.removeColor(FACEBOOK_COLOR);
        }else if(pkgName.contains("com.google.android.gm")){
            pulse.removeColor(GMAIL_COLOR);
        }else if(pkgName.contains("com.google.android.talk")){
            pulse.removeColor(HANGOUTS_COLOR);
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn, RankingMap rankingMap) {
        String pkgName = sbn.getPackageName();
        if(pkgName.equalsIgnoreCase("com.facebook.orca")){
            pulse.removeColor(FACEBOOK_COLOR);
        }else if(pkgName.contains("com.google.android.gm")){
            pulse.removeColor(GMAIL_COLOR);
        }else if(pkgName.contains("com.google.android.talk")){
            pulse.removeColor(HANGOUTS_COLOR);
        }
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn, RankingMap rankingMap) {
        Log.d("WATCH", sbn.getPackageName());
        String pkgName = sbn.getPackageName();

        if(pkgName.equalsIgnoreCase("com.facebook.orca")){
            pulse.pushColor(FACEBOOK_COLOR);
            activeNotifs.add(pkgName);
            Log.d("WATCH", "facebook thing. Color is " + sbn.getNotification().color);
        }else if(pkgName.contains("com.google.android.gm")){
            pulse.pushColor(GMAIL_COLOR);
            activeNotifs.add(pkgName);
            Log.d("WATCH", "google thing. Color is " + sbn.getNotification().color);
        }else if(pkgName.contains("com.google.android.talk")){
            pulse.pushColor(HANGOUTS_COLOR);
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
                    Log.d("WATCH", "time between next event and now: " + f + ", percent " + actual);
                }
            }
        });
    }
}