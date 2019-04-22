package com.example.databaseprototype;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);


        //Note - you shouldn't do this kind of stuff on the main thread in production. This should go onto a background thread. This is just for example purposes.
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(EventEntry.COL_EVENT_NAME,"battery_level");
        contentValues.put(EventEntry.COL_BATTERY_LEVEL,69);
//        contentValues.put(BookEntry.COL_BOOKNAME, "Life of Pi 5");
//        contentValues.put(BookEntry.COL_DESCRIPTION, "Yann Martel's Life of Pi is the story of a young man who survives a harrowing shipwreck and months in a lifeboat with a large Bengal tiger named Richard Parker. The beginning of the novel covers Pi's childhood and youth.");
//        contentValues.put(BookEntry.COL_NO_PAGES, 24325);
        //contentValues.put(BookEntry.COL_RATING, 8);
        //contentValues.put(BookEntry.COL_CALCULATED_RATING, 8 * 24325);
        long result = database.insert(EventEntry.TABLE_NAME, null, contentValues);
        if(result==-1){
            Toast.makeText(MainActivity.this, "Not Done",
                    Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(MainActivity.this, "Data Inserted",
                    Toast.LENGTH_LONG).show();
        }

//
//        ContentValues contentValues2 = new ContentValues();
//        contentValues2.put(BookEntry.COL_BOOKNAME, "Gone Girl 5");
//        contentValues2.put(BookEntry.COL_DESCRIPTION, "In Carthage, Mo., former New York-based writer Nick Dunne (Ben Affleck) and his glamorous wife Amy (Rosamund Pike) present a portrait of a blissful marriage to the public. However, when Amy goes missing on the couple's fifth wedding anniversary, Nick becomes the prime suspect in her disappearance. The resulting police pressure and media frenzy cause the Dunnes' image of a happy union to crumble, leading to tantalizing questions about who Nick and Amy truly are.");
//        contentValues2.put(BookEntry.COL_NO_PAGES, 45425);
//        //contentValues2.put(BookEntry.COL_RATING, 5);
//        contentValues.put(BookEntry.COL_CALCULATED_RATING, 5 * 45245);
//        result = database.insert(BookEntry.TABLE_NAME, null, contentValues2);
//
//        if(result==-1){
//            Toast.makeText(MainActivity.this, "Not Done",
//                    Toast.LENGTH_LONG).show();
//        }
//        else {
//            Toast.makeText(MainActivity.this, "Data Inserted",
//                    Toast.LENGTH_LONG).show();
//        }


/*
        ContentValues contentValues3 = new ContentValues();
        contentValues2.put(BookEntry.COL_BOOKNAME, "New Book");
        contentValues2.put(BookEntry.COL_DESCRIPTION, "In Carthage, Mo., former New York-based writer Nick Dunne (Ben Affleck) and his glamorous wife Amy (Rosamund Pike) present a portrait of a blissful marriage to the public. However, when Amy goes missing on the couple's fifth wedding anniversary, Nick becomes the prime suspect in her disappearance. The resulting police pressure and media frenzy cause the Dunnes' image of a happy union to crumble, leading to tantalizing questions about who Nick and Amy truly are.");
        contentValues2.put(BookEntry.COL_NO_PAGES, 4425);
        contentValues2.put(BookEntry.COL_RATING, 3);
        contentValues.put(BookEntry.COL_CALCULATED_RATING, 5 * 4425);
        result = database.insert(BookEntry.TABLE_NAME, null, contentValues3);

        if(result==-1){
            Toast.makeText(MainActivity.this, "Not Done",
                    Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(MainActivity.this, "Data Inserted",
                    Toast.LENGTH_LONG).show();
        }*/

//        SQLiteDatabase db = databaseHelper.getWritableDatabase();
//        Cursor c = db.query(EventEntry.TABLE_NAME, null, null, null, null, null, null);
//
//        String books = "";
//        while (c.moveToNext()) {
//            String bookName = c.getString(c.getColumnIndex(BookEntry.COL_BOOKNAME));
//            String bookDescription = c.getString(c.getColumnIndex(BookEntry.COL_DESCRIPTION));
//            String rating = "Nothing";
//            //String rating = c.getString(c.getColumnIndex(BookEntry.COL_RATING));
//            String calculatedCol = c.getString(c.getColumnIndex(BookEntry.COL_CALCULATED_RATING));
//
//            books += bookName + " - " + bookDescription + ".[RATING]:" + rating + "[CALCULATED RATING]:" + calculatedCol +  "\r\n";
//            Log.d(TAG, "Book Name:" + bookName +"And Rating: "+ rating + " From table: "+BookEntry.TABLE_NAME);
//
//        }
//        c.close();



    }
}
