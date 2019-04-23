package com.example.edios;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateEventActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    public int REQUEST_READ_PHONE_STATE = 1;
    int count_if_selection = 0;
    int count_then_selection = 0;
    ListView ListView_If_Selected;
    ListView ListView_Then_Selected;
    List<String> IF_LIST = new ArrayList<>();
    List<String> THEN_LIST = new ArrayList<>();
    SQLiteDatabase database;
    DatabaseHelper databaseHelper;
    Context context;
    //Calendar c;
    int y=0,m=0,d=0,h=0,min=0,battery_level,recent_recipe_id;
    Boolean send_call_logs;
    String alarm_msg,server_address,post_msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Database helper Class
        databaseHelper = DatabaseHelper.getInstance(this);
        database = databaseHelper.getWritableDatabase();

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menuNav = navigationView.getMenu();
        MenuItem create_event_item = menuNav.findItem(R.id.nav_create_event_id);
        create_event_item.setChecked(true);

        final SharedPreferences sharedPreferences = getSharedPreferences("DATA_CREATE_EVENT",Context.MODE_PRIVATE);
        count_if_selection = sharedPreferences.getInt("count_if_selection",0);
        count_then_selection = sharedPreferences.getInt("count_then_selection",0);
        battery_level = sharedPreferences.getInt("battery_level",0);
        y = sharedPreferences.getInt("year",0);
        m = sharedPreferences.getInt("month",0);
        d = sharedPreferences.getInt("day",0);
        h = sharedPreferences.getInt("hour",0);
        min = sharedPreferences.getInt("min",0);
        send_call_logs = sharedPreferences.getBoolean("send_call_logs ",false);
        alarm_msg = sharedPreferences.getString("alarm_msg","null");
        server_address = sharedPreferences.getString("server_address","null");
        post_msg = sharedPreferences.getString("post_msg","null");

        for(int i=0; i<count_if_selection; i++){
            IF_LIST.add(sharedPreferences.getString("IF_LIST_"+Integer.toString(i),""));
        }
        for(int i=0; i<count_then_selection; i++){
            THEN_LIST.add(sharedPreferences.getString("THEN_LIST_"+Integer.toString(i),""));
        }

//        context = this;
//        c = Calendar.getInstance();
//        c.set(2019, 4, 16);
//        c.set(Calendar.HOUR_OF_DAY, 10);
//        c.set(Calendar.MINUTE, 27);
//        c.set(Calendar.SECOND, 0);
        //executeRecipe = new ExecuteRecipe(context,c);


        ListView_If_Selected = findViewById(R.id.ListView_for_if_selected);
        ListView_Then_Selected = findViewById(R.id.ListView_for_then_selected);

        CustomAdapterForIfSelected customAdapterForIfSelection = new CustomAdapterForIfSelected();
        ListView_If_Selected.setAdapter(customAdapterForIfSelection);
        CustomAdapterForThenSelected customAdapterForThenSelected = new CustomAdapterForThenSelected();
        ListView_Then_Selected.setAdapter(customAdapterForThenSelected);



        TextView select_for_if_selection = findViewById(R.id.select_for_if_selection);
        select_for_if_selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_state();
                Intent intent = new Intent(CreateEventActivity.this,IfSelectionActivity.class);

                startActivity(intent);
            }
        });
        TextView select_for_then_selection = findViewById(R.id.select_for_then_selection);

        select_for_then_selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count_if_selection > 0) {
                    save_state();
                    Intent intent = new Intent(CreateEventActivity.this, ThenSelectionActivity.class);
                    startActivity(intent);
                }
            }
        });

        Button save_button = findViewById(R.id.save_button);

        save_button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick (View view){
                    if (count_if_selection > 0 && count_then_selection > 0) {
                        //Also save to MY_EVENTS activity
                        //go to MY_EVENT activity
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();

                        //Recipe Insertion
                        ContentValues r_contentValues = new ContentValues();
                        r_contentValues.put("event_list","null");
                        r_contentValues.put("service_list","null");
                        //long result = 0
                        long r_result = database.insert("recipe", null, r_contentValues);
                        if(r_result==-1){
                            Toast.makeText(CreateEventActivity.this, "Not Done",
                                    Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(CreateEventActivity.this, "Data Inserted",
                                    Toast.LENGTH_LONG).show();
                        }

            //Fetching the Recent Recipe ID
                        SQLiteDatabase db = databaseHelper.getWritableDatabase();
                        Cursor r_cursor = db.rawQuery("select max(recipe_id) from recipe",null);
                        //String books = "";
                        if (r_cursor.moveToFirst()){
                            do {
                                // Passing values
                                recent_recipe_id = r_cursor.getInt(0);
                                // Do something Here with values
                            } while(r_cursor.moveToNext());
                        }
                        r_cursor.close();

                        for(int i=0;i<IF_LIST.size();i++) {
                            if (IF_LIST.get(i).equals("D & T")) {
                                //Insert data
                                Calendar c = Calendar.getInstance();
                                c.set(y,m,d,h,min);
                                long cc = c.getTimeInMillis();
                                String datetime = String.valueOf(cc);
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("event_id",recent_recipe_id);
                                contentValues.put("event_name","date_time");
                                contentValues.put("date_time",datetime);
                                //long result = 0
                                long result = database.insert("events", null, contentValues);
                                if(result==-1){
                                    Toast.makeText(CreateEventActivity.this, "Not Done",
                                            Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(CreateEventActivity.this, "Data Inserted",
                                            Toast.LENGTH_LONG).show();
                                }
                                Intent backgroundService = new Intent(getApplicationContext(), AlarmService.class);
                                backgroundService.putExtra("calender",c);
                                //backgroundService.putExtra("service_name","notification");
                                startService(backgroundService);
                            }
                            else if (IF_LIST.get(i).equals("B Power")) {
                                //Insert data
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("event_id",recent_recipe_id);
                                contentValues.put("event_name","battery_level");
                                Log.e("Before inserting", "onClick: Battery Level is "+battery_level);
                                contentValues.put("battery_level",battery_level);
                                long result = database.insert("events", null, contentValues);
                                if(result==-1){
                                    Toast.makeText(CreateEventActivity.this, "Not Done",
                                            Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(CreateEventActivity.this, "Data Inserted",
                                            Toast.LENGTH_LONG).show();
                                }


                            }
                            else if (IF_LIST.get(i).equals("M Call")) {
                                //Insert data
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("event_id",recent_recipe_id);
                                contentValues.put("event_name","missed_call");
                                Log.e("Before inserting", "onClick: Missed call Entry");
                                contentValues.put("missed_call",1);
                                long result = database.insert("events", null, contentValues);
                                if(result==-1){
                                    Toast.makeText(CreateEventActivity.this, "Not Done",
                                            Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(CreateEventActivity.this, "Data Inserted",
                                            Toast.LENGTH_LONG).show();
                                }


                                //Starting of Missed Call Service
                                Intent backgroundService = new Intent(getApplicationContext(), MissedCallBackgroundService.class).putExtra("name","send_data");
                                Intent intent=new Intent();
                                intent.addCategory(Intent.CATEGORY_DEFAULT);
                                intent.setAction("android.intent.action.PHONE_STATE");
                                int permissionCheck = ContextCompat.checkSelfPermission(CreateEventActivity.this, Manifest.permission.READ_PHONE_STATE);
                                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(CreateEventActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
                                    Log.e("Hey", "in IF ");
                                } else {
                                    //TODO
                                    Log.e("Hey", "in Else");
                                    Log.e("Hey", "Permission value: "+permissionCheck);
                                    startService(backgroundService);
                                }
                                Log.d("Service", "Service Started");
                            }
                        }

                        for(int i=0;i<THEN_LIST.size();i++) {
                            if (THEN_LIST.get(i).equals("H Post")) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("service_id",recent_recipe_id);
                                contentValues.put("service_name","H_Post");
                                Log.e("Before inserting", "onClick: All data is "+server_address+post_msg+send_call_logs);
                                contentValues.put("ip_address",server_address);
                                contentValues.put("http_data",post_msg);
                                int temp=0;
                                if(send_call_logs==true) {
                                    temp=1;
                                }
                                contentValues.put("call_logs",temp);
                                long result = database.insert("services", null, contentValues);
                                if(result==-1){
                                    Toast.makeText(CreateEventActivity.this, "Not Done",
                                            Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(CreateEventActivity.this, "Data Inserted",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                            else if (THEN_LIST.get(i).equals("Alarm")) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("service_id",recent_recipe_id);
                                contentValues.put("service_name","Alarm");
                                Log.e("Before inserting", "onClick: All data is "+alarm_msg);
                                contentValues.put("alarm_message",alarm_msg);
                                long result = database.insert("services", null, contentValues);
                                if(result==-1){
                                    Toast.makeText(CreateEventActivity.this, "Not Done",
                                            Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(CreateEventActivity.this, "Data Inserted",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        }

//                        Intent if_intent = getIntent();
//                        int number_of_events = if_intent.getIntExtra("NUMBER_OF_EVENTS",0);
//                        Log.d("Save Button Pressed", "onClick: "+number_of_events);
//
//                        for(int i=1;i<=number_of_events;i++) {
//                            String str = if_intent.getStringExtra("KEY_EVENT_NAME_"+i);
//                            if(str.equals("D & T")) {
//                                Log.d("inside Loop", "onClick: D and T");
//                                int y = if_intent.getIntExtra("YEAR",0);
//                                int m = if_intent.getIntExtra("MONTH",0);
//                                int d = if_intent.getIntExtra("DAY",0);
//                                int h = if_intent.getIntExtra("HOUR",0);
//                                int min = if_intent.getIntExtra("MINUTE",0);
//                                Calendar c = Calendar.getInstance();
//                                c.set(y,m,d,h,min);
//                                long cc = c.getTimeInMillis();
//                                String datatime = String.valueOf(cc);
//                                ContentValues contentValues = new ContentValues();
//                                contentValues.put("date_time",datatime);
//                                long result = database.insert("events", null, contentValues);
//                                if(result==-1){
//                                    Toast.makeText(CreateEventActivity.this, "Not Done",
//                                            Toast.LENGTH_LONG).show();
//                                }
//                                else {
//                                    Toast.makeText(CreateEventActivity.this, "Data Inserted",
//                                            Toast.LENGTH_LONG).show();
//                                }
//                            }
//
//
//                            if(str.equals("B Power")) {
//                                int progress = if_intent.getIntExtra("YEAR",0);
//                            }
//
//
//
//                        }

                        Toast.makeText(CreateEventActivity.this, "Saved!!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(CreateEventActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
        });


        //get Intent, It will get intent only from if_selection nad then_selection. And intent will only contain a string (name of event or service)
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null){
            String fromWhichActivity = intent.getExtras().getString("FROM_WHICH","");
            if (fromWhichActivity.equals("IfSelectionActivity")) {

                int number_of_events_received = intent.getExtras().getInt("NUMBER_OF_EVENTS");
                for (int i = 1; i <= number_of_events_received; i++) {
                    count_if_selection++;
                    IF_LIST.add(intent.getExtras().getString("KEY_EVENT_NAME_" + i, ""));
                }

                ((CustomAdapterForIfSelected) ListView_If_Selected.getAdapter()).notifyDataSetChanged();
                save_state();

                for (int i = 1; i <= number_of_events_received; i++) {
                    if (intent.getExtras().getString("KEY_EVENT_NAME_" + i, "").equals("D & T")) {
                        y = intent.getExtras().getInt("YEAR", 0);
                        m = intent.getExtras().getInt("MONTH", 0);
                        d = intent.getExtras().getInt("DAY", 0);
                        h = intent.getExtras().getInt("HOUR", 0);
                        min = intent.getExtras().getInt("MINUTE", 0);
                        //Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                        //c.set(y, m, d, h, min);
                        //Log.d("TEST ", "" + c.get(Calendar.YEAR));
                    } else if (intent.getExtras().getString("KEY_EVENT_NAME_" + i, "").equals("B Power")) {
                        battery_level = intent.getIntExtra("BATTERY_PERCENTAGE",0);
                        Log.e("inside Battery Intent", "onCreate: "+battery_level);
                    }
                }
            }
            else if(fromWhichActivity.equals("ThenSelectionActivity")){
                int number_of_services_received = intent.getExtras().getInt("NUMBER_OF_SERVICES");
                for(int i = 1; i<= number_of_services_received; i++){
                    count_then_selection++;
                    THEN_LIST.add(intent.getExtras().getString("KEY_SERVICE_NAME_"+i,""));
                }
                ((CustomAdapterForThenSelected) ListView_Then_Selected.getAdapter()).notifyDataSetChanged();

                save_state();

                for (int i = 1; i <= number_of_services_received; i++) {
                    if (intent.getExtras().getString("KEY_SERVICE_NAME_" + i, "").equals("Alarm")) {
                         alarm_msg = intent.getStringExtra("ALARM_MESSAGE");
                        //Log.d("TEST ", "" + c.get(Calendar.YEAR));
                    } else if (intent.getExtras().getString("KEY_SERVICE_NAME_" + i, "").equals("H Post")) {
                        send_call_logs = intent.getBooleanExtra("SEND_CALL_LOGS_?",false);
                        server_address = intent.getStringExtra("SERVER_ADDRESS");
                        post_msg = intent.getStringExtra("MESSAGE_TO_POST_ON_SERVER");
                        Log.e("inside H post Intent", "onCreate: "+server_address+send_call_logs+post_msg);
                    }
                }
            }
        }

        save_state();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Inside onResume", "onResume: ");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_event, menu);
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

        /*if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    public void save_state(){
        SharedPreferences sharedPreferences = getSharedPreferences("DATA_CREATE_EVENT",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        for(int i=0; i<IF_LIST.size(); i++){
            editor.putString("IF_LIST_"+Integer.toString(i),IF_LIST.get(i));
        }
        for(int i=0; i<THEN_LIST.size(); i++){
            editor.putString("THEN_LIST_"+Integer.toString(i),THEN_LIST.get(i));
        }

        editor.putInt("count_if_selection",count_if_selection);
        editor.putInt("count_then_selection",count_then_selection);
        editor.putInt("battery_level",battery_level);
        editor.putInt("year",y);
        editor.putInt("month",m);
        editor.putInt("day",d);
        editor.putInt("hour",h);
        editor.putInt("min",min);
        editor.putBoolean("send_call_logs",send_call_logs);
        editor.putString("post_msg",post_msg);
        editor.putString("server_address",server_address);
        editor.putString("alarm_msg",alarm_msg);
        editor.apply();

    }

    public void clear_state(){
        SharedPreferences sharedPreferences = getSharedPreferences("DATA_CREATE_EVENT",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }


    class CustomAdapterForIfSelected extends BaseAdapter{

        @Override
        public int getCount() {
            return IF_LIST.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.if_selection_list_view_row_layout,null);
            ImageButton imageButton = view.findViewById(R.id.CancelButton_inside_listView_if_selection);
            TextView textView = view.findViewById(R.id.textView_inside_listView_if_selection);

            textView.setText(IF_LIST.get(i));
            return view;
        }
    }

    class CustomAdapterForThenSelected extends BaseAdapter{

        @Override
        public int getCount() {
            return THEN_LIST.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.then_selection_list_view_row_layout,null);
            ImageButton imageButton = view.findViewById(R.id.CancelButton_inside_listView_then_selection);
            TextView textView = view.findViewById(R.id.textView_inside_listView_then_selection);

            textView.setText(THEN_LIST.get(i));
            return view;
        }
    }



    public void cancel_if_selected_click(View view){
        RelativeLayout parent_row = (RelativeLayout) view.getParent();

        TextView textView_in_parent_row = (TextView) parent_row.getChildAt(0);
        String text = (String)textView_in_parent_row.getText();
        IF_LIST.remove(text);
        count_if_selection--;
        ((CustomAdapterForIfSelected) ListView_If_Selected.getAdapter()).notifyDataSetChanged();
        save_state();
    }
    public void cancel_then_selected_click(View view){
        RelativeLayout parent_row = (RelativeLayout) view.getParent();

        TextView textView_in_parent_row = (TextView) parent_row.getChildAt(0);
        String text = (String)textView_in_parent_row.getText();
        THEN_LIST.remove(text);
        count_then_selection--;
        ((CustomAdapterForThenSelected) ListView_Then_Selected.getAdapter()).notifyDataSetChanged();
        save_state();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            clear_state();
            Intent intent = new Intent(CreateEventActivity.this,MainActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }
}

