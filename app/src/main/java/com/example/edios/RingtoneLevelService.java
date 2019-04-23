package com.example.edios;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * This service is started when an Alarm has been raised
 *
 * We pop a notification into the status bar for the user to click on
 * When the user clicks the notification a new activity is opened
 */

public class RingtoneLevelService extends Service {

    /**
     * Class for clients to access
     */

    Boolean aBoolean = false;
    public class ServiceBinder extends Binder {
        RingtoneLevelService getService() {
            return RingtoneLevelService.this;
        }
    }


    // Name of an intent extra we can use to identify if this service was started to create a notification
    public static final String INTENT_NOTIFY = "com.example.edios.INTENT_NOTIFY";
    // The system notification manager
    private AudioManager myAudioManager;

    @Override
    public void onCreate() {
        Log.i("NotifyService", "onCreate()");
        //mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        myAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        aBoolean = intent.getBooleanExtra(INTENT_NOTIFY, false);

        if (intent!=null) {
            if(aBoolean) {
                float volume = intent.getFloatExtra("volume_level",1);
                //float volume = 1;
                setRingtoneLevel(volume);
            }
        }
        // If this service was started by out AlarmTask intent then we want to show our notification


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
    private void setRingtoneLevel(float percent) {

        int currentVolume = myAudioManager.getStreamVolume(AudioManager.STREAM_RING);
        int maxVolume = myAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        //float percent = 0.7f;
        int setVolume = (int) (maxVolume*percent);
        myAudioManager.setStreamVolume(AudioManager.STREAM_RING, setVolume, 0);
        Log.d("volume","volume is set");
        // Stop the service when we are finished
        stopSelf();
    }
}

