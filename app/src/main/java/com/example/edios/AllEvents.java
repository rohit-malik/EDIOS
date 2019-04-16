package com.example.edios;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class AllEvents extends AppCompatActivity {

    int NumberOfTotalEvents = 2;
    ListView listViewForAllEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_events);

        listViewForAllEvents = findViewById(R.id.listView_for_all_events);
        CustomAdapterForAllEvents customAdapterForAllEvents = new CustomAdapterForAllEvents();
        listViewForAllEvents.setAdapter(customAdapterForAllEvents);

    }

    class CustomAdapterForAllEvents extends BaseAdapter{

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {

            if (position == 0) {
                return 0;
            } else {
                return 1;
            }
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
            Log.d("position", Integer.toString(position));
            int type = getItemViewType(position);

            if (view == null) {

                if (type == 0) {
                    //Time date row
                    view = getLayoutInflater().inflate(R.layout.activity_event_missed_call, null);

                    ImageButton description = view.findViewById(R.id.description_event_missed_call);
                    description.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            final Dialog dialog = new Dialog(AllEvents.this, android.R.style.Theme_Translucent_NoTitleBar);
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
                } else if (type == 1) {
                    view = getLayoutInflater().inflate(R.layout.activity_event_time_date,null);

                    ImageButton description = view.findViewById(R.id.description_event_time_date);
                    description.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final Dialog dialog = new Dialog(AllEvents.this,android.R.style.Theme_Translucent_NoTitleBar);
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
            }

            return view;
        }
    }
}
