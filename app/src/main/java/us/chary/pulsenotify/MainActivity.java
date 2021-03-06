package us.chary.pulsenotify;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Activity activity = this;
        Toast.makeText(this, "Before anything, pair the phone with the speaker via bluetooth. Then come back to this app and turn on notification listening for this app. If it's already on, turn it off and then back on again. Then press the back button on the phone to start the demo.", 7000).show();
        Intent intent=new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        startActivity(intent);

        findViewById(R.id.thing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationListener.pulse.connect(activity);
            }
        });

        findViewById(R.id.fb_notif).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationListener.sendNotif(activity, "notification from facebook");
            }
        });

        findViewById(R.id.gmail_notif).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationListener.sendNotif(activity, "notification from gmail");
            }
        });

        findViewById(R.id.hangouts_notif).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationListener.sendNotif(activity, "notification from hangouts");
            }
        });

        NotificationListener.registerCalendarNotifierListenerOnBtn(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
