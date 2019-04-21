package com.example.edios;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "Broadcast received!!!!.", Toast.LENGTH_LONG).show();
        Log.d("inside broadcast", "Broadcast received");
        String service_name= intent.getStringExtra("service_name");
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
    }
}
