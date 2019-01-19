package uz.shukurov.izohlilugat;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Farouk Touzi.
 */
public class NotificationView extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_view_activity);

        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Dismiss Notification
        notificationmanager.cancel(0);

        // Retrieve the data from MainActivity.java
        Intent i = getIntent();

        String title = i.getStringExtra("something");
        String text = i.getStringExtra("text");

        // Locate the TextView
        TextView titleTv = (TextView) findViewById(R.id.title);
        TextView textTv = (TextView) findViewById(R.id.text);

        // Set the data into TextView
        titleTv.setText(title);
        textTv.setText(text);
    }
}
