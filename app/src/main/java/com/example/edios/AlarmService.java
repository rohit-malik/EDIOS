package com.example.edios;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Calendar;

public class AlarmService extends Service implements Serializable {

    private Calendar date;
    // The android system alarm manager
    private AlarmManager am;
    private String service_name;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //return super.onStartCommand(intent, flags, startId);
        date = (Calendar) intent.getSerializableExtra("calender");
        service_name = (String) intent.getSerializableExtra("service_name");
        Intent my_intent = new Intent(this, AlarmBroadcastReceiver.class);
        my_intent.putExtra("service_name",service_name);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 234324243, my_intent, 0);
        //Toast.makeText(context, "Broadcast received!!!!.", Toast.LENGTH_LONG).show();
        Log.d("inside alarmservice", "alarm service running");
        am.set(AlarmManager.RTC, date.getTimeInMillis(), pendingIntent);
        Log.d("after setting alarm","alarm set");
        Log.d("date time", String.valueOf(date.getTime()));
        return START_NOT_STICKY;
    }
}
