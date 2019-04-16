package com.example.edios;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CreateEventActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    int count_if_selection = 0;
    int count_then_selection = 0;
    ListView ListView_If_Selected;
    ListView ListView_Then_Selected;
    List<String> IF_LIST = new ArrayList<>();
    List<String> THEN_LIST = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        for(int i=0; i<count_if_selection; i++){
            IF_LIST.add(sharedPreferences.getString("IF_LIST_"+Integer.toString(i),""));
        }
        for(int i=0; i<count_then_selection; i++){
            THEN_LIST.add(sharedPreferences.getString("THEN_LIST_"+Integer.toString(i),""));
        }


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
                //remove the lines below
                //IF_LIST.add("Hello1"+Integer.toString(count_if_selection));
                //count_if_selection++;
                //((CustomAdapterForIfSelected) ListView_If_Selected.getAdapter()).notifyDataSetChanged();
            }
        });
        TextView select_for_then_selection = findViewById(R.id.select_for_then_selection);

        select_for_then_selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(count_if_selection > 0) {
                    save_state();
                    Intent intent = new Intent(CreateEventActivity.this,ThenSelectionActivity.class);
                    startActivity(intent);
                    //remove the following lines
                    //THEN_LIST.add("Hello11"+Integer.toString(count_then_selection));
                    //count_then_selection++;
                    //((CustomAdapterForThenSelected) ListView_Then_Selected.getAdapter()).notifyDataSetChanged();
                //}
            }
        });

        Button save_button = findViewById(R.id.save_button);

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count_if_selection>0 && count_then_selection>0) {
                    //save this event into database
                    //Also save to MY_EVENTS activity
                    //go to MY_EVENT activity
                    //remove everything from sharedPreference
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();

                    Toast.makeText(CreateEventActivity.this,"Saved!!",Toast.LENGTH_SHORT).show();
                }
            }
        });


        //get Intent, It will get intent only from if_selection nad then_selection. And intent will only contain a string (name of event or service)
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null){
            String fromWhichActivity = intent.getExtras().getString("FROM_WHICH","");
            if (fromWhichActivity.equals("IfSelectionActivity")){
                count_if_selection++;
                IF_LIST.add(intent.getExtras().getString("KEY_EVENT_NAME",""));
                ((CustomAdapterForIfSelected) ListView_If_Selected.getAdapter()).notifyDataSetChanged();
                save_state();
            }
            else if(fromWhichActivity.equals("then_selection")){
                count_then_selection++;
                THEN_LIST.add(intent.getExtras().getString("SERVICE_NAME",""));
                ((CustomAdapterForThenSelected) ListView_Then_Selected.getAdapter()).notifyDataSetChanged();
                save_state();
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
            ImageButton imageButton = view.findViewById(R.id.imageButton_inside_listView_if_selection);
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
            ImageButton imageButton = view.findViewById(R.id.imageButton_inside_listView_then_selection);
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
}
