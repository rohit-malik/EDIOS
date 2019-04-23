package com.example.edios;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class MyRecipes extends AppCompatActivity {

    int NumberOfTotalMyReceipies = 0;
    ListView listViewForMyReceipies;

    List<String> IF_LIST;
    List<String> THEN_LIST;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes);

        listViewForMyReceipies = findViewById(R.id.list_view_for_my_receipies);
        CustomAdapterForMyReceipies customAdapterForMyReceipies = new CustomAdapterForMyReceipies();
        listViewForMyReceipies.setAdapter(customAdapterForMyReceipies);

        //reset the listView from dataBase

        Intent intent = getIntent();

        if(intent.getStringExtra("FROM_WHICH").equals("CreateEventActivity")){
            NumberOfTotalMyReceipies++;

            String IF_LIST = intent.getStringExtra("IF_LIST");
            String THEN_LIST = intent.getStringExtra("THEN_LIST");


            //save this recipe in database
            //reset the list view

            ((CustomAdapterForMyReceipies) listViewForMyReceipies.getAdapter()).notifyDataSetChanged();

        }


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

            return view;

        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
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


    public void delete_recipe_button(View view){


        ((CreateEventActivity.CustomAdapterForIfSelected) listViewForMyReceipies.getAdapter()).notifyDataSetChanged();
        //save_state();
    }
}
