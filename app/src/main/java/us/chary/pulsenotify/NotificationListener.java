package us.chary.pulsenotify;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;

import com.harman.pulsesdk.PulseColor;

import java.util.ArrayList;

public class NotificationListener extends NotificationListenerService {

    public static final int FACEBOOK_COLOR = 0x0011ff;
    public static final int GMAIL_COLOR = 0xff1100;
    public static final int HANGOUTS_COLOR = 0x11ff11;
    public static int notifId = 90;
    public static Pulse pulse = new Pulse();
    private ArrayList<String> activeNotifs = new ArrayList<>();


    public NotificationListener() {
        Log.d("WATCH", "instantiated");
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
        Bundle bundle = sbn.getNotification().extras;
        String title = bundle.getString("android.title");

        if(pkgName.equalsIgnoreCase("com.facebook.orca") || title.contains("facebook")){
            pulse.removeColor(FACEBOOK_COLOR);
        }else if(pkgName.contains("com.google.android.gm") || title.contains("gmail")){
            pulse.removeColor(GMAIL_COLOR);
        }else if(pkgName.contains("com.google.android.talk") || title.contains("hangouts")){
            pulse.removeColor(HANGOUTS_COLOR);
        }
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn, RankingMap rankingMap) {
        Log.d("WATCH", sbn.getPackageName());
        String pkgName = sbn.getPackageName();
        Bundle bundle = sbn.getNotification().extras;
        String title = bundle.getString("android.title");

        if(pkgName.equalsIgnoreCase("com.facebook.orca") || title.contains("facebook")){
            pulse.pushColor(FACEBOOK_COLOR);
            activeNotifs.add(pkgName);
            Log.d("WATCH", "facebook thing. Color is " + sbn.getNotification().color);
        }else if(pkgName.contains("com.google.android.gm") || title.contains("gmail")){
            pulse.pushColor(GMAIL_COLOR);
            activeNotifs.add(pkgName);
            Log.d("WATCH", "google thing. Color is " + sbn.getNotification().color);
        }else if(pkgName.contains("com.google.android.talk") || title.contains("hangouts")){
            pulse.pushColor(HANGOUTS_COLOR);
            activeNotifs.add(pkgName);
            Log.d("WATCH", "google thing. Color is " + sbn.getNotification().color);
        }
    }

    public static void sendNotif(Activity activity, String message){
        Notification notification = new
                NotificationCompat.Builder(activity)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(message)
                .setContentText("Notification Message")
                .setTicker("Ticker Text")
                .setAutoCancel(true)
                .setNumber(1)
                .build();

        ((NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE)).notify(notifId, notification);
        notifId++;
    }
    public static void registerCalendarNotifierListenerOnBtn(final Activity activity) {
        activity.findViewById(R.id.calendar_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                long f = new CalendarHelper().getTimeBetweenNowAndNextEvent(activity);
                long f = 15000000;
                if(f < 27000000){
                    float percent = (float) f / (float) 27000000;
                    int actual = 100 - ((int) (percent * 100));
                    Log.d("WATCH", "time between next event and now: " + f + ", percent " + actual);

                    while(actual < 100){
                        if(actual+ 3 < 100){
                            pulse.phi.SetBrightness(actual + 3);
                        }

                        PulseColor[] g = new PulseColor[99];
                        for(int i = 0; i < 99; i++){
                            if(i < actual){
                                g[i] = pulse.int2pc(0xBF5FFF);
                            }else{
                                g[i] = pulse.int2pc(0x000000);
                            }
                        }
                        pulse.phi.SetColorImage(g);
                        actual++;
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    for(int i = 0; i < 11;i++){
                        if(i % 2 == 0){
                            pulse.phi.SetBackgroundColor(pulse.int2pc(0xBF5FFF), true);
                        }else{
                            pulse.phi.SetBackgroundColor(pulse.int2pc(0xffffff), true);
                        }
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }
}