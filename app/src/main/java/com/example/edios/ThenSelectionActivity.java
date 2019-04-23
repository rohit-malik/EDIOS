package com.example.edios;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;


public class ThenSelectionActivity extends AppCompatActivity {

    int NumberOfTotalServices = 3;
    ListView listViewForThenSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_then_selection);

        listViewForThenSelection = findViewById(R.id.listView_for_then_selection_activity);
        CustomAdapterForThenSelectionActivity customAdapterForThenSelectionActivity = new CustomAdapterForThenSelectionActivity();
        listViewForThenSelection.setAdapter(customAdapterForThenSelectionActivity);

        Button proceed = findViewById(R.id.proceed_btn_in_ThenSelectionActivity);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int NumberOfSelectedServices = 0;

                Intent intent = new Intent(ThenSelectionActivity.this, CreateEventActivity.class);
                intent.putExtra("FROM_WHICH", "ThenSelectionActivity");


                View view1 = getViewByPosition(0,listViewForThenSelection);
                CheckBox checkBox1 = view1.findViewById(R.id.checkBox_alarm_notify);
                if(checkBox1.isChecked()){
                    NumberOfSelectedServices++;

                    intent.putExtra("KEY_SERVICE_NAME_"+NumberOfSelectedServices, "Alarm");

                }
                View view2 = getViewByPosition(1,listViewForThenSelection);
                CheckBox checkBox2 = view2.findViewById(R.id.checkBox_post_http);
                if(checkBox2.isChecked()){
                    NumberOfSelectedServices++;
                    intent.putExtra("KEY_SERVICE_NAME_"+NumberOfSelectedServices,"H Post");

                }
                View view3 = getViewByPosition(2,listViewForThenSelection);
                CheckBox checkBox3 = view3.findViewById(R.id.checkBox_battery_power);
                if(checkBox3.isChecked()){
                    NumberOfSelectedServices++;
                    intent.putExtra("KEY_SERVICE_NAME_"+NumberOfSelectedServices,"Set Volume");

                    SeekBar seekBar = view3.findViewById(R.id.seekBar_battery_power);
                    int progress = seekBar.getProgress();
                    intent.putExtra("SET_VOLUME_TO_THIS",progress);
                }


                intent.putExtra("NUMBER_OF_SERVICES",NumberOfSelectedServices);

                startActivity(intent);
            }
        });

    }




    class CustomAdapterForThenSelectionActivity extends BaseAdapter{

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
            else{
                return 2;
            }

            //return (position == this.getCount()-1) ? 0 : 1;
        }

        @Override
        public int getCount() {
            return NumberOfTotalServices;
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

            if(view == null){

                if(type == 0){
                    //Time date row
                    view = getLayoutInflater().inflate(R.layout.row_layout_service_alarm_notify,null);

                    ImageButton description = view.findViewById(R.id.description_alarm);
                    description.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            final Dialog dialog = new Dialog(ThenSelectionActivity.this,android.R.style.Theme_Translucent_NoTitleBar);
                            dialog.setContentView(R.layout.description_dialog);
                            Window window = dialog.getWindow();


                            if (window != null) {
                                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                window.setGravity(Gravity.CENTER);
                            }


                            dialog.show();
                            TextView txt_description = dialog.findViewById(R.id.txt_description_in_description_dialog);
                            txt_description.setText(R.string.description_alarm);
                            dialog.findViewById(R.id.GotIt_btn_in_description_dialog).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                        }
                    });


                    EditText alarmText =  findViewById(R.id.input_alarm_message);




                }
                else if(type == 1){
                    view = getLayoutInflater().inflate(R.layout.row_layout_service_post_http,null);

                    ImageButton description = view.findViewById(R.id.description_post_http);
                    description.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final Dialog dialog = new Dialog(ThenSelectionActivity.this,android.R.style.Theme_Translucent_NoTitleBar);
                            dialog.setContentView(R.layout.description_dialog);
                            Window window = dialog.getWindow();

                            if (window != null) {
                                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                window.setGravity(Gravity.CENTER);
                            }


                            dialog.show();
                            TextView txt_description = dialog.findViewById(R.id.txt_description_in_description_dialog);
                            txt_description.setText(R.string.description_post_http);
                            dialog.findViewById(R.id.GotIt_btn_in_description_dialog).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                        }
                    });

                }
                else if(type == 2){
                    view = getLayoutInflater().inflate(R.layout.row_layout_event_battery_power,null);

                    TextView textView = view.findViewById(R.id.textView_battery_power);
                    textView.setText(R.string.ringtone_volume);

                    ImageButton description = view.findViewById(R.id.description_battery_power);
                    description.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final Dialog dialog = new Dialog(ThenSelectionActivity.this,android.R.style.Theme_Translucent_NoTitleBar);
                            dialog.setContentView(R.layout.description_dialog);
                            Window window = dialog.getWindow();

                            if (window != null) {
                                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                window.setGravity(Gravity.CENTER);
                            }


                            dialog.show();
                            TextView txt_description = dialog.findViewById(R.id.txt_description_in_description_dialog);
                            txt_description.setText(R.string.description_set_volume);
                            dialog.findViewById(R.id.GotIt_btn_in_description_dialog).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

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
