package com.example.edios;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    DatabaseHelper databaseHelper;
    int event_id;
    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "Broadcast received!!!!.", Toast.LENGTH_LONG).show();
        Log.d("inside broadcast", "Broadcast received");
        //String service_name= intent.getStringExtra("service_name");
        String calender_time = intent.getStringExtra("calender_time");
        String[] params = new String[]{ calender_time };
        databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor r_cursor = db.rawQuery("select event_id from events where event_name = 'date_time' and date_time = ?",params);
        //String books = "";
        if (r_cursor.moveToFirst()){
            do {
                // Passing values
                event_id = r_cursor.getInt(0);
                // Do something Here with values
            } while(r_cursor.moveToNext());
        }
        r_cursor.close();
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
                    Intent my_intent = new Intent(context, NotifyService.class);
                    my_intent.putExtra(NotifyService.INTENT_NOTIFY, true);
                    my_intent.putExtra("message",notification_message);
                    context.startService(my_intent);
                }
                else if(service_name.equals("H_Post")){
                    int call_logs = r_cursor2.getInt(8);
                    Log.d("H post","h post service is executed");
                    Log.d("Call logs is selected", String.valueOf(call_logs));

                    Intent my_intent = new Intent(context, SendDataService.class);
                    //my_intent.putExtra(RingtoneLevelService.INTENT_NOTIFY, true);
                    Log.d("Hello", "onReceive: else if of send_data");
                    context.startService(my_intent);
                }
                else if(service_name.equals("set_volume")){
                    Log.d("Set volume","set volume service is executed");
                    float volume_level = (float) (r_cursor2.getInt(11)/100.0);
                    Log.d("volume level is", String.valueOf(volume_level));
                    Intent my_intent = new Intent(context, RingtoneLevelService.class);
                    my_intent.putExtra("volume_level",volume_level);
                    my_intent.putExtra(RingtoneLevelService.INTENT_NOTIFY, true);
                    context.startService(my_intent);
                }

                // Do something Here with values
            } while(r_cursor2.moveToNext());
        }
        r_cursor2.close();

        /*
        if(service_name.equals("notification")) {
            Intent my_intent = new Intent(context, NotifyService.class);
            my_intent.putExtra(NotifyService.INTENT_NOTIFY, true);
            context.startService(my_intent);
        }
        else if(service_name.equals("set_volume")){
            Intent my_intent = new Intent(context, RingtoneLevelService.class);
            my_intent.putExtra(RingtoneLevelService.INTENT_NOTIFY, true);
            context.startService(my_intent);
        }
        else if(service_name.equals("send_data")) {
            Intent my_intent = new Intent(context, SendDataService.class);
            //my_intent.putExtra(RingtoneLevelService.INTENT_NOTIFY, true);
            Log.d("Hello", "onReceive: else if of send_data");
            context.startService(my_intent);
        }
        */
    }
}
