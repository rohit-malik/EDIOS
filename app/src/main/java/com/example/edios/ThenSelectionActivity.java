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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;



public class ThenSelectionActivity extends AppCompatActivity {

    int NumberOfTotalEvents = 3;
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
                //Here Save the event selected Event Data in DataBase
                //Then go back to createEventActivity
                Intent intent = new Intent(ThenSelectionActivity.this,CreateEventActivity.class);
                intent.putExtra("KEY_SERVICE_NAME","Notify");
                intent.putExtra("KEY_EVENT_NAME","D & T");
                intent.putExtra("FROM_WHICH","ThenSelectionActivity");
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
                else if(type == 1 || type == 2){
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

                    EditText serverAddressText =  findViewById(R.id.input_server_address);
                    EditText postMessageText =  findViewById(R.id.input_text_message);


                }


            }

            return view;
        }
    }
}
