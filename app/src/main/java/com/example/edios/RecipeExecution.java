package com.example.edios;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;

public class RecipeExecution extends AppCompatActivity {


    public int REQUEST_READ_PHONE_STATE = 1;
    private BroadcastReceiver broadcastReceiver;
    Context context = RecipeExecution.this;
    String string = "Hello";
    ScheduleClient scheduleClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_execution);
        //scheduleClient = new ScheduleClient(this);
        scheduleClient = new ScheduleClient(RecipeExecution.this);
        scheduleClient.doBindService();
        //cc =
        Log.e("Inside Excecute", "ExecuteServices: " );
        Intent intent = getIntent();
        Button button = findViewById(R.id.button);
        Button send = findViewById(R.id.sendData);
        Button br_on = findViewById(R.id.br_on);
        Button br_off = findViewById(R.id.br_off);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                c.set(2019, 4, 16);
                c.set(Calendar.HOUR_OF_DAY,11);
                c.set(Calendar.MINUTE, 58);

                c.set(Calendar.SECOND, 0);
                Intent intent = getIntent();
                // Ask our service to set an alarm for that date, this activity talks to the client that talks to the service
                scheduleClient.setAlarmForNotification(c);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SendData.class);
                startActivity(intent);
            }
        });


        br_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backgroundService = new Intent(getApplicationContext(), MissedCallBackgroundService.class).putExtra("name","send_data");
                Intent intent=new Intent();
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setAction("android.intent.action.PHONE_STATE");
                int permissionCheck = ContextCompat.checkSelfPermission(RecipeExecution.this, Manifest.permission.READ_PHONE_STATE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(RecipeExecution.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
                    Log.e("Hey", "in IF ");
                } else {
                    //TODO
                    Log.e("Hey", "in Else");
                    Log.e("Hey", "Permission value: "+permissionCheck);
                    startService(backgroundService);
                }
                Log.d("Service", "Service Started");
            }
        });

        br_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backgroundService = new Intent(getApplicationContext(), MissedCallBackgroundService.class);
                stopService(backgroundService);
                Log.d("Service", "Service Stopped");
            }
        });
        // Ask our service to set an alarm for that date, this activity talks to the client that talks to the service
        //scheduleClient.setAlarmForNotification(c);
        //scheduleClient.setMissedCallService(intent);
        // Notify the user what they just did
        //scheduleClient.setRingtoneForNotification(cc,0.5f);
        //scheduleClient.setRingtoneForNotification(cc);
        //onAlarm();
    }


//    @Override
//    protected void onStop() {
//        // When our activity is stopped ensure we also stop the connection to the service
//        // this stops us leaking our activity into the system *bad*
//        if(scheduleClient != null)
//            scheduleClient.doUnbindService();
//        //super.onStop();
//    }
}
