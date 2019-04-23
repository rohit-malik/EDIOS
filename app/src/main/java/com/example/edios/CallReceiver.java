package com.example.edios;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class CallReceiver extends BroadcastReceiver {

    DatabaseHelper databaseHelper;
    static boolean ring = false;
    static boolean callReceived = false;
    public String callerPhoneNumber;
    List<Integer> event_id_list;

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context mContext, Intent intent) {
        event_id_list = new ArrayList<>();
        databaseHelper = DatabaseHelper.getInstance(mContext);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor r_cursor = db.rawQuery("select event_id from events where event_name = 'missed_call'",null);
        //String books = "";
        if (r_cursor.moveToFirst()){
            do {
                // Passing values
                int event_id = r_cursor.getInt(0);
                event_id_list.add(event_id);
                // Do something Here with values
            } while(r_cursor.moveToNext());
        }
        r_cursor.close();



        Log.e(TAG, "onReceive: "+intent.toString());
        // Get the current Phone State
        //String dara = intent.getData();
        MissedCallBackgroundService missedCallBackgroundService = new MissedCallBackgroundService();
        String str = missedCallBackgroundService.getStr();
        Log.e(TAG, "onReceive: "+str);
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        if (state == null)
            return;
        // If phone state "Ringing"
        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            ring = true;
            // Get the Caller's Phone Number
            Bundle bundle = intent.getExtras();
            assert bundle != null;
            callerPhoneNumber = bundle.getString("incoming_number");
            Toast.makeText(mContext, "It is ringing: " + callerPhoneNumber, Toast.LENGTH_LONG).show();
            Log.d(TAG, "it's Ringing");
        }


        // If incoming call is received
        if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            Toast.makeText(mContext, "It is receives : " + callerPhoneNumber, Toast.LENGTH_LONG).show();
            Log.d(TAG, "it's Received");
            callReceived = true;
        }


        // If phone is Idle
        if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
            // If phone was ringing(ring=true) and not received(callReceived=false) , then it is a missed call
            if (!callReceived)
                if (ring) {
                    Log.d(TAG, "it's Missed Call");


                    for(int i=0;i<event_id_list.size();i++){
                        int event_id = event_id_list.get(i);
                        String[] params2 = new String[]{String.valueOf(event_id)};
                        Cursor r_cursor2 = db.rawQuery("select * from services where service_id = ?",params2);
                        //String books = "";
                        if (r_cursor2.moveToFirst()){
                            do {
                                // Passing values
                                int service_id = r_cursor2.getInt(0);
                                String service_name = r_cursor2.getString(1);
                                if(service_name.equals("Alarm")){
                                    String notification_message = r_cursor2.getString(9);
                                    Intent my_intent = new Intent(mContext, NotifyService.class);
                                    my_intent.putExtra(NotifyService.INTENT_NOTIFY, true);
                                    my_intent.putExtra("message",notification_message);
                                    mContext.startService(my_intent);
                                }
                                else if(service_name.equals("H_Post")){
                                    int call_logs = r_cursor2.getInt(8);
                                    String http_data = r_cursor2.getString(7);
                                    String ip_address = r_cursor2.getString(6);
                                    Log.d("H post","h post service is executed");
                                    Log.d("Call logs is selected", String.valueOf(call_logs));
                                    if (call_logs==1) {
                                        //Run call log service
                                        Intent my_intent = new Intent(mContext, SendDataService.class);
                                        my_intent.putExtra("ip_address",ip_address);
                                        my_intent.putExtra("call_logs",call_logs);
                                        //my_intent.putExtra(RingtoneLevelService.INTENT_NOTIFY, true);
                                        Log.d("Hello", "onReceive: else if of send_data");
                                        mContext.startService(my_intent);
                                    }
                                    else if(call_logs==0) {
                                        //Run text data service
                                        Intent my_intent = new Intent(mContext, SendTextService.class);
                                        my_intent.putExtra("http_data",http_data);
                                        my_intent.putExtra("ip_address",ip_address);
                                        //my_intent.putExtra("call_logs",call_logs);
                                        //my_intent.putExtra(RingtoneLevelService.INTENT_NOTIFY, true);
                                        Log.d("Hello", "onReceive: else if of send_data");
                                        mContext.startService(my_intent);
                                    }
                                    //Intent my_intent = new Intent(mContext, SendDataService.class);
                                    //my_intent.putExtra("http_data",http_data);
                                    //my_intent.putExtra("ip_address",ip_address);
                                    //my_intent.putExtra("call_logs",call_logs);
                                    //my_intent.putExtra(RingtoneLevelService.INTENT_NOTIFY, true);
                                    //Log.d("Hello", "onReceive: else if of send_data");
                                    //mContext.startService(my_intent);
                                }
                                else if(service_name.equals("set_volume")){
                                    Log.d("Set volume","set volume service is executed");
                                    float volume_level = (float) (r_cursor2.getInt(11)/100.0);
                                    Log.d("volume level is", String.valueOf(volume_level));
                                    Intent my_intent = new Intent(mContext, RingtoneLevelService.class);
                                    my_intent.putExtra("volume_level",volume_level);
                                    my_intent.putExtra(RingtoneLevelService.INTENT_NOTIFY, true);
                                    mContext.startService(my_intent);
                                }

                                // Do something Here with values
                            } while(r_cursor2.moveToNext());
                        }
                        r_cursor2.close();

                    }




                    //Intent my_intent = new Intent(mContext, NotifyService.class);
                    //my_intent.putExtra(NotifyService.INTENT_NOTIFY, true);
                    //mContext.startService(my_intent);


//
//                    Intent my_intent = new Intent(mContext, RingtoneLevelService.class);
//                    my_intent.putExtra(RingtoneLevelService.INTENT_NOTIFY, true);
//                    mContext.startService(my_intent);


//
//                    Intent my_intent = new Intent(mContext, SendDataService.class);
//                    //my_intent.putExtra(RingtoneLevelService.INTENT_NOTIFY, true);
//                    Log.d("Hello", "onReceive: else if of send_data");
//                    mContext.startService(my_intent);

                    Toast.makeText(mContext, "It was A MISSED CALL from : " + callerPhoneNumber, Toast.LENGTH_LONG).show();
                }
        }

    }


}

