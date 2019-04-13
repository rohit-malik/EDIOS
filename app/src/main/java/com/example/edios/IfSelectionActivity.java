package com.example.edios;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.TimeZone;

public class IfSelectionActivity extends AppCompatActivity {


    int NumberOfTotalEvents = 2;
    ListView listViewForIfSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_if_selection);

        listViewForIfSelection = findViewById(R.id.listView_for_if_selection_activity);
        CustomAdapterForIfSelectionActivity customAdapterForIfSelectionActivity = new CustomAdapterForIfSelectionActivity();
        listViewForIfSelection.setAdapter(customAdapterForIfSelectionActivity);

    }











    class CustomAdapterForIfSelectionActivity extends BaseAdapter{

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return (position == this.getCount()-1) ? 0 : 1;
        }

        @Override
        public int getCount() {
            return NumberOfTotalEvents;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            int type = getItemViewType(position);

            if(view == null){
                if(type == 0){
                    //Time date row
                    view = getLayoutInflater().inflate(R.layout.row_layout_event_time_date,null);
                    String hour[] = new String[25];
                    String minute[] = new String[61];
                    String day[] = new String[32];
                    String month[] = new String[13];
                    String year[] = new String[10];

                    Spinner spinner_hour = view.findViewById(R.id.spinner_hour);
                    Spinner spinner_minute = view.findViewById(R.id.spinner_minute);
                    Spinner spinner_day = view.findViewById(R.id.spinner_day);
                    Spinner spinner_month = view.findViewById(R.id.spinner_month);
                    Spinner spinner_year = view.findViewById(R.id.spinner_year);

                    ImageButton description = view.findViewById(R.id.description_time_date);

                    CheckBox checkBox = view.findViewById(R.id.checkBox_time_date);

                    Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                    int current_year = calendar.get(Calendar.YEAR);
                    int current_month = calendar.get(Calendar.MONTH);
                    int current_day = calendar.get(Calendar.DAY_OF_MONTH);
                    int current_hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int current_minute = calendar.get(Calendar.MINUTE);

                    for (int i = current_year; i<current_year+10; i++){
                        year[i-current_year] = Integer.toString(i);
                    }
                    for (int i = 0; i<=12; i++){
                        if(i == 0){
                            month[i] = Integer.toString(current_month);
                            continue;
                        }
                        month[i] = Integer.toString(i);
                    }
                    for (int i = 0; i<=31; i++){
                        if(i == 0){
                            day[i] = Integer.toString(current_day);
                            continue;
                        }
                        day[i] = Integer.toString(i);
                    }
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

                    ArrayAdapter adapter_hour = new ArrayAdapter(IfSelectionActivity.this,android.R.layout.simple_spinner_item,hour);
                    adapter_hour.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_hour.setAdapter(adapter_hour);

                    ArrayAdapter adapter_minute = new ArrayAdapter(IfSelectionActivity.this,android.R.layout.simple_spinner_item,minute);
                    adapter_minute.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_minute.setAdapter(adapter_minute);

                    ArrayAdapter adapter_year = new ArrayAdapter(IfSelectionActivity.this,android.R.layout.simple_spinner_item,year);
                    adapter_year.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_year.setAdapter(adapter_year);

                    ArrayAdapter adapter_day = new ArrayAdapter(IfSelectionActivity.this,android.R.layout.simple_spinner_item,day);
                    adapter_day.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_day.setAdapter(adapter_day);

                    ArrayAdapter adapter_month = new ArrayAdapter(IfSelectionActivity.this,android.R.layout.simple_spinner_item,month);
                    adapter_month.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_month.setAdapter(adapter_month);


                }
                else if(type == 1){
                    view = getLayoutInflater().inflate(R.layout.row_layout_missed_call,null);
                }

            }

            return view;
        }
    }
}
