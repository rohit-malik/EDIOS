package com.example.edios;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * This service is started when an Alarm has been raised
 *
 * We pop a notification into the status bar for the user to click on
 * When the user clicks the notification a new activity is opened
 */

public class NotifyService extends Service {

    /**
     * Class for clients to access
     */
    public class ServiceBinder extends Binder {
        NotifyService getService() {
            return NotifyService.this;
        }
    }

    // Unique id to identify the notification.
    private static final int NOTIFICATION = 123;
    // Name of an intent extra we can use to identify if this service was started to create a notification
    public static final String INTENT_NOTIFY = "com.example.edios.INTENT_NOTIFY";
    // The system notification manager
    private NotificationManager mNotificationManager;

    @Override
    public void onCreate() {
        Log.i("NotifyService", "onCreate()");
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        // If this service was started by out AlarmTask intent then we want to show our notification
        if(intent.getBooleanExtra(INTENT_NOTIFY, false))
            showNotification();

        // We don't care if this service is stopped as we have already delivered our notification
        return START_NOT_STICKY;
        //return START_STICKY;
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients
    private final IBinder mBinder = new ServiceBinder();

    /**
     * Creates a notification and shows it in the OS drag-down status bar
     */
    private void showNotification() {
        // This is the 'title' of the notification
        CharSequence title = "Alarm!!";
        // This is the icon to use on the notification
        //int icon = R.drawable.ic_dialog_alert;
        // This is the scrolling text of the notification
        CharSequence contentTitle = "Hello";
        CharSequence contentText = "Your notification time is upon us.";

        String CHANNEL_ID = "1";
        String CHANNEL_NAME = "alarmNotification";
        String CHANNEL_DESCRIPTION = "Alarm channel";

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        // What time to show on the notification
        //long time = System.currentTimeMillis();




        //Notification notification = new Notification(icon, text, time);

        // The PendingIntent to launch our activity if the user selects this notification
        /*
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);


        Notification.Builder builder = new Notification.Builder(this);
        builder.setAutoCancel(true);
        builder.setTicker("this is ticker text");
        builder.setContentTitle("Alarm Notification");
        builder.setContentText("You have a new message");
        builder.setSmallIcon(R.drawable.ic_dialog_alert);
        builder.setContentIntent(contentIntent);
        builder.setOngoing(true);
        builder.setSubText("This is subtext...");   //API level 16
        builder.setNumber(100);
        //builder.build();

        builder.setSound(alarmSound);
        //myNotication = builder.getNotification();

        */

        //Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ getApplicationContext().getPackageName() + "/" + R.raw.ringtone);
        // Set the info for the views that show in the notification panel.
        //notification.setLatestEventInfo(this, title, text, contentIntent);

        // Clear the notification when it is pressed
        //notification.flags |= Notification.FLAG_AUTO_CANCEL;

        //NotificationManager mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            mChannel.setLightColor(Color.GRAY);
            mChannel.enableLights(true);
            mChannel.setDescription(CHANNEL_DESCRIPTION);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            mChannel.setSound(soundUri, audioAttributes);

            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel( mChannel );
                Log.d("api level","api level is above 26");
            }
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_dialog_alert)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ic_launcher))
                .setTicker(title)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setAutoCancel(true)
                .setLights(0xff0000ff, 300, 1000) // blue color
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(contentIntent);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Log.d("api level","api level is below 26");
            mBuilder.setSound(soundUri);
        }





        // Send the notification to the system.
        mNotificationManager.notify(NOTIFICATION, mBuilder.build());

        // Stop the service when we are finished
        //stopSelf();
    }
}
