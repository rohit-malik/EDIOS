package com.example.edios;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class MissedCallBackgroundService extends Service {


    private String str = null;
    private CallReceiver misscallreceiver;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //* This is Previous Code
        // Create an IntentFilter instance.
        this.str = intent.getStringExtra("name");
//        Intent intent1 = new Intent(this,CallReceiver.class);
//        intent1.putExtra("name",str);


        IntentFilter intentFilter = new IntentFilter();
        // Add network connectivity change action.
        intentFilter.addAction("android.intent.action.PHONE_STATE");
        //intentFilter.addAction("android.intent.action.PHONE_STATE");
        // Set broadcast receiver priority.
        intentFilter.setPriority(100);
        // Create a network change broadcast receiver.
        misscallreceiver = new CallReceiver();


        // Register the broadcast receiver with the intent filter object.
        registerReceiver(misscallreceiver , intentFilter);
//        Intent intent = new Intent("android.intent.action.PHONE_STATE");
//        intent.putExtra("Name","Send_data");
//        sendBroadcast(intent);
        Log.e(TAG, "Registered ");
        //Log.w(misscallreceiver , "Service onCreate: screenOnOffReceiver is registered.");
        return START_STICKY;
        //return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //Unregister screenOnOffReceiver when destroy.
        if(misscallreceiver!=null)
        {
            unregisterReceiver(misscallreceiver);
            Log.d("Service Destroyed", "onDestroy: Service Unregistered" );
            //Log.d(misscallreceiver.SCREEN_TOGGLE_TAG, "Service onDestroy: screenOnOffReceiver is unregistered.");
        }
    }


    public String getStr() {
        return this.str;
    }

}
