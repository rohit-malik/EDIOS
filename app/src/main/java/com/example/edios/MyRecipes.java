package com.example.edios;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyRecipes extends AppCompatActivity {

    int NumberOfTotalMyReceipies = 0;
    ListView listViewForMyReceipies;
    DatabaseHelper databaseHelper;
    List<String> IF_LIST = new ArrayList<>();
    List<String> THEN_LIST = new ArrayList<>();
    List<Integer> RECIPE_ID_LIST = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes);

        listViewForMyReceipies = findViewById(R.id.list_view_for_my_receipies);
        CustomAdapterForMyReceipies customAdapterForMyReceipies = new CustomAdapterForMyReceipies();
        listViewForMyReceipies.setAdapter(customAdapterForMyReceipies);



        databaseHelper = DatabaseHelper.getInstance(this);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor r_cursor = db.rawQuery("select count(recipe_id) from recipe",null);
        if (r_cursor.moveToFirst()){
            do {
                // Passing values
                NumberOfTotalMyReceipies = r_cursor.getInt(0);
                // Do something Here with values
            } while(r_cursor.moveToNext());
        }
        r_cursor.close();
        Cursor r_cursor2 = db.rawQuery("select * from recipe",null);
        if (r_cursor2.moveToFirst()){
            do {
                // Passing values
                int recipe_id = r_cursor2.getInt(0);
                String event_list = r_cursor2.getString(1);
                String service_list = r_cursor2.getString(2);

                RECIPE_ID_LIST.add(recipe_id);
                IF_LIST.add(event_list);
                THEN_LIST.add(service_list);
            } while(r_cursor2.moveToNext());
        }
        r_cursor2.close();







        Button done_button = findViewById(R.id.done_btn_in_my_receipies);
        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(MyRecipes.this,MainActivity.class);
                startActivity(intent2);
            }
        });




    }


    class CustomAdapterForMyReceipies extends BaseAdapter{

        @Override
        public int getCount() {
            return NumberOfTotalMyReceipies;
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
        public View getView(int i, View view, ViewGroup viewGroup) {

            view = getLayoutInflater().inflate(R.layout.row_layout_recipe_all,null);

            TextView textView = view.findViewById(R.id.recipe_name);
            textView.setText("Recipe "+i);

            textView = view.findViewById(R.id.if_list_here);
            textView.setText(IF_LIST.get(i));

            textView = view.findViewById(R.id.then_list_here);
            textView.setText(THEN_LIST.get(i));

            final int x = RECIPE_ID_LIST.get(i);
            ImageView delete_btn = view.findViewById(R.id.delete_recipe);
            delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    databaseHelper = DatabaseHelper.getInstance(MyRecipes.this);
                    SQLiteDatabase db = databaseHelper.getWritableDatabase();
                    String[] params = new String[]{ String.valueOf(x) };
                    int result = db.delete("recipe","recipe_id = ?", params);
                    int result2 = db.delete("events","event_id = ?", params);
                    int result3 = db.delete("services","service_id = ?", params);
                    Log.d("printlist","" + IF_LIST.toString());
                    Log.d("list", "onClick: outsite delete" + IF_LIST.contains("[M Call]"));
                    //Stoping Missed Call Service
                    if(IF_LIST.contains("[M Call]")) {
                        Intent backgroundService = new Intent(getApplicationContext(), MissedCallBackgroundService.class);
                        stopService(backgroundService);
                        Log.d("Service", "Service Stopped");
                    }
                    Intent intent = new Intent(MyRecipes.this,MyRecipes.class);
                    startActivity(intent);
                }

            });



            return view;

        }


    }





}
