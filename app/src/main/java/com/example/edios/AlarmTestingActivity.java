package com.example.edios;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;

public class AlarmTestingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    // This is a handle so that we can call methods on our service
    private ScheduleClient scheduleClient;
    // This is the date picker used to select the date for our notification
    private DatePicker picker;
    private Spinner spinner_hour;
    private Spinner spinner_minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_testing);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Create a new service client and bind our activity to this service
        scheduleClient = new ScheduleClient(this);
        scheduleClient.doBindService();

        // Get a reference to our date picker
        picker = (DatePicker) findViewById(R.id.scheduleTimePicker);
        spinner_hour = findViewById(R.id.spinner_hour);
        spinner_minute = findViewById(R.id.spinner_minute);
        String hour[] = new String[25];
        String minute[] = new String[61];

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int current_year = calendar.get(Calendar.YEAR);
        int current_month = calendar.get(Calendar.MONTH);
        int current_day = calendar.get(Calendar.DAY_OF_MONTH);
        int current_hour = calendar.get(Calendar.HOUR_OF_DAY);
        int current_minute = calendar.get(Calendar.MINUTE);


        for (int i = 0; i<25; i++){
            if(i == 0){
                hour[i] = Integer.toString(current_hour);
                continue;
            }
            hour[i] = Integer.toString(i-1);
        }
        for (int i = 0; i<61; i++){
            if(i == 0){
                minute[i] = Integer.toString(current_minute);
                continue;
            }
            minute[i] = Integer.toString(i-1);
        }



        ArrayAdapter adapter_hour = new ArrayAdapter(AlarmTestingActivity.this,android.R.layout.simple_spinner_item,hour);
        adapter_hour.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_hour.setAdapter(adapter_hour);

        ArrayAdapter adapter_minute = new ArrayAdapter(AlarmTestingActivity.this,android.R.layout.simple_spinner_item,minute);
        adapter_minute.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_minute.setAdapter(adapter_minute);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.alarm_testing, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void onDateSelectedButtonClick(View v){
        // Get the date from our datePicker
        String shour = spinner_hour.getSelectedItem().toString();
        int hour = Integer.parseInt(shour);
        String sminute = spinner_minute.getSelectedItem().toString();
        int minute = Integer.parseInt(sminute);
        int day = picker.getDayOfMonth();
        int month = picker.getMonth();
        int year = picker.getYear();
        // Create a new calendar set to the date chosen
        // we set the time to midnight (i.e. the first minute of that day)
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        // Ask our service to set an alarm for that date, this activity talks to the client that talks to the service
        scheduleClient.setAlarmForNotification(c);
        scheduleClient.setRingtoneForNotification(c);
        // Notify the user what they just did
        Toast.makeText(this, "Notification set for: "+ day +"/"+ (month+1) +"/"+ year, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        // When our activity is stopped ensure we also stop the connection to the service
        // this stops us leaking our activity into the system *bad*
        if(scheduleClient != null)
            scheduleClient.doUnbindService();
        super.onStop();
    }


}
