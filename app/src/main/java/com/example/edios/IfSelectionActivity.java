package com.example.edios;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.TimeZone;

public class IfSelectionActivity extends AppCompatActivity {


    int NumberOfTotalEvents = 3;
    ListView listViewForIfSelection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_if_selection);

        listViewForIfSelection = findViewById(R.id.listView_for_if_selection_activity);
        CustomAdapterForIfSelectionActivity customAdapterForIfSelectionActivity = new CustomAdapterForIfSelectionActivity();
        listViewForIfSelection.setAdapter(customAdapterForIfSelectionActivity);

        final Button proceed = findViewById(R.id.proceed_btn_in_ifSelectionActivity);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int NumberOfSelectedEvents = 0;

                Intent intent = new Intent(IfSelectionActivity.this, CreateEventActivity.class);
                intent.putExtra("FROM_WHICH", "IfSelectionActivity");


                View view1 = getViewByPosition(1,listViewForIfSelection);
                CheckBox checkBox1 = view1.findViewById(R.id.checkBox_time_date);
                if(checkBox1.isChecked()){
                    NumberOfSelectedEvents++;
                    Spinner spinner_hour = view1.findViewById(R.id.spinner_hour);
                    Spinner spinner_minute = view1.findViewById(R.id.spinner_minute);
                    Spinner spinner_day = view1.findViewById(R.id.spinner_day);
                    Spinner spinner_month = view1.findViewById(R.id.spinner_month);
                    Spinner spinner_year = view1.findViewById(R.id.spinner_year);
                    int y = Integer.parseInt(spinner_year.getSelectedItem().toString());
                    int d = Integer.parseInt(spinner_day.getSelectedItem().toString());
                    int m = Integer.parseInt(spinner_month.getSelectedItem().toString());
                    int h = Integer.parseInt(spinner_hour.getSelectedItem().toString());
                    int min = Integer.parseInt(spinner_minute.getSelectedItem().toString());
                    Calendar calendarToSend = Calendar.getInstance();
                    Log.d("inside if selection:",String.valueOf(h));

                    calendarToSend.set(y,m-1,d,h,min);
                    intent.putExtra("KEY_EVENT_NAME_"+NumberOfSelectedEvents, "D & T");
                    intent.putExtra("YEAR",calendarToSend.get(Calendar.YEAR));
                    intent.putExtra("MONTH",calendarToSend.get(Calendar.MONTH));
                    intent.putExtra("DAY",calendarToSend.get(Calendar.DAY_OF_MONTH));
                    intent.putExtra("HOUR",calendarToSend.get(Calendar.HOUR_OF_DAY));
                    Log.d("inside if selection:",String.valueOf(calendarToSend.get(Calendar.HOUR_OF_DAY)));
                    intent.putExtra("MINUTE",calendarToSend.get(Calendar.MINUTE));

                }
                View view2 = getViewByPosition(0,listViewForIfSelection);
                CheckBox checkBox2 = view2.findViewById(R.id.checkBox_missed_calls);
                if(checkBox2.isChecked()){
                    NumberOfSelectedEvents++;
                    intent.putExtra("KEY_EVENT_NAME_"+NumberOfSelectedEvents,"M Call");

                }

                View view3 = getViewByPosition(2,listViewForIfSelection);
                CheckBox checkBox3 = view3.findViewById(R.id.checkBox_battery_power);
                if(checkBox3.isChecked()){
                    NumberOfSelectedEvents++;
                    intent.putExtra("KEY_EVENT_NAME_"+NumberOfSelectedEvents,"B Power");

                    SeekBar seekBar = view3.findViewById(R.id.seekBar_battery_power);
                    TextView bar_percentage = view3.findViewById(R.id.seekbar_percentage_in_battery_power_layout);
                    int progress = seekBar.getProgress();
                    intent.putExtra("BATTERY_PERCENTAGE",progress);
                }

                intent.putExtra("NUMBER_OF_EVENTS",NumberOfSelectedEvents);

                startActivity(intent);

            }
        });

    }




    class CustomAdapterForIfSelectionActivity extends BaseAdapter{

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public int getItemViewType(int position) {

            if(position == 0){
                return 0;
            }
            else if(position == 1){
                return 1;
            }
            else if(position == 2){
                return 2;
            }
            else
                return 3;

            //return (position == this.getCount()-1) ? 0 : 1;
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
            Log.d("position",Integer.toString(position));
            int type = getItemViewType(position);

            if(view == null) {
                if (type == 1) {
                    //Time date row
                    view = getLayoutInflater().inflate(R.layout.row_layout_event_time_date, null);

                    ImageButton description = view.findViewById(R.id.description_time_date);
                    description.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            final Dialog dialog = new Dialog(IfSelectionActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
                            dialog.setContentView(R.layout.description_dialog);
                            Window window = dialog.getWindow();


                            if (window != null) {
                                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                window.setGravity(Gravity.CENTER);
                            }


                            dialog.show();


                            dialog.findViewById(R.id.GotIt_btn_in_description_dialog).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                        }
                    });


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

                    Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                    int current_year = calendar.get(Calendar.YEAR);
                    int current_month = calendar.get(Calendar.MONTH);
                    int current_day = calendar.get(Calendar.DAY_OF_MONTH);
                    int current_hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int current_minute = calendar.get(Calendar.MINUTE);

                    for (int i = current_year; i < current_year + 10; i++) {
                        year[i - current_year] = Integer.toString(i);
                    }
                    for (int i = 0; i <= 12; i++) {
                        if (i == 0) {
                            month[i] = Integer.toString(current_month+1);
                            continue;
                        }
                        month[i] = Integer.toString(i);
                    }
                    for (int i = 0; i <= 31; i++) {
                        if (i == 0) {
                            day[i] = Integer.toString(current_day);
                            continue;
                        }
                        day[i] = Integer.toString(i);
                    }
                    for (int i = 0; i < 25; i++) {
                        if (i == 0) {
                            hour[i] = Integer.toString(current_hour);
                            continue;
                        }
                        hour[i] = Integer.toString(i - 1);
                    }
                    for (int i = 0; i < 61; i++) {
                        if (i == 0) {
                            minute[i] = Integer.toString(current_minute);
                            continue;
                        }
                        minute[i] = Integer.toString(i - 1);
                    }

                    ArrayAdapter adapter_hour = new ArrayAdapter(IfSelectionActivity.this, android.R.layout.simple_spinner_item, hour);
                    adapter_hour.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_hour.setAdapter(adapter_hour);

                    ArrayAdapter adapter_minute = new ArrayAdapter(IfSelectionActivity.this, android.R.layout.simple_spinner_item, minute);
                    adapter_minute.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_minute.setAdapter(adapter_minute);

                    ArrayAdapter adapter_year = new ArrayAdapter(IfSelectionActivity.this, android.R.layout.simple_spinner_item, year);
                    adapter_year.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_year.setAdapter(adapter_year);

                    ArrayAdapter adapter_day = new ArrayAdapter(IfSelectionActivity.this, android.R.layout.simple_spinner_item, day);
                    adapter_day.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_day.setAdapter(adapter_day);

                    ArrayAdapter adapter_month = new ArrayAdapter(IfSelectionActivity.this, android.R.layout.simple_spinner_item, month);
                    adapter_month.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_month.setAdapter(adapter_month);

                    CheckBox checkBox = view.findViewById(R.id.checkBox_time_date);

                    checkBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                        }
                    });


                } else if (type == 0) {
                    view = getLayoutInflater().inflate(R.layout.row_layout_missed_call, null);

                    ImageButton description = view.findViewById(R.id.description_missed_calls);
                    description.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final Dialog dialog = new Dialog(IfSelectionActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
                            dialog.setContentView(R.layout.description_dialog);
                            Window window = dialog.getWindow();

                            if (window != null) {
                                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                window.setGravity(Gravity.CENTER);
                            }


                            dialog.show();
                            TextView txt_description = dialog.findViewById(R.id.txt_description_in_description_dialog);
                            txt_description.setText(R.string.description_missed_calls);
                            dialog.findViewById(R.id.GotIt_btn_in_description_dialog).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                        }
                    });


                    CheckBox checkBox = view.findViewById(R.id.checkBox_missed_calls);
                    checkBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                } else if (type == 2) {
                    view = getLayoutInflater().inflate(R.layout.row_layout_event_battery_power, null);
                    ImageButton description = view.findViewById(R.id.description_battery_power);
                    description.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final Dialog dialog = new Dialog(IfSelectionActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
                            dialog.setContentView(R.layout.description_dialog);
                            Window window = dialog.getWindow();

                            if (window != null) {
                                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                window.setGravity(Gravity.CENTER);
                            }


                            dialog.show();
                            TextView txt_description = dialog.findViewById(R.id.txt_description_in_description_dialog);
                            txt_description.setText(R.string.description_battery_power);
                            dialog.findViewById(R.id.GotIt_btn_in_description_dialog).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                        }
                    });

                    CheckBox checkBox = view.findViewById(R.id.checkBox_battery_power);
                    checkBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });

                    final TextView bar_percentge = view.findViewById(R.id.seekbar_percentage_in_battery_power_layout);
                    SeekBar seekBar = view.findViewById(R.id.seekBar_battery_power);
                    bar_percentge.setText(String.valueOf(seekBar.getProgress()));

                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            bar_percentge.setText(String.valueOf(progress));
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });


                }
            }

            return view;
        }
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;
        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}
