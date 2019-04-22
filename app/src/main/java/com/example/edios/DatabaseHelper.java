package com.example.edios;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
/*
 * DataBase helper class which helps to Handle database queries
 *
 * */
public class DatabaseHelper extends SQLiteOpenHelper {


    /* Used for Other Classes */
    private static DatabaseHelper mInstance = null;

    /* Maintains the DataBase Version */
    private static final int DATABASE_VERSION = 1;

    //Name of DataBase
    private static final String DATABASE_NAME = "database.db";

    private final Context context;
    private static final String TAG = DatabaseHelper.class.getName();

    private DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static synchronized DatabaseHelper getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new DatabaseHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }


    public static void clearInstance() {
        mInstance = null;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create all The tables using a function which reads the sql file line by line
        //String str = "CREATE TABLE IF NOT EXISTS events (event_id INTEGER PRIMARY KEY, event_name TEXT NOT NULL, battery_level INTEGER, caller_number TEXT, call_time NUMERIC, date_time TEXT, location TEXT, FOREIGN KEY(event_id) REFERENCES recipe(recipe_id));";
        //db.execSQL("");
        String filename = "tables.sql";
        Log.d(TAG, "onCreate: Inside Oncreate of DatabaseHelper");
        readAndExecuteSQLScript(db, context, filename);
        
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(TAG, "Updating table from " + oldVersion + " to " + newVersion);
        // You will not need to modify this unless you need to do some android specific things.
        // When upgrading the database, all you need to do is add a file to the assets folder and name it:
        // from_1_to_2.sql with the version that you are upgrading to as the last version.
        try {
            for (int i = oldVersion; i < newVersion; ++i) {
                @SuppressLint("DefaultLocale") String migrationName = String.format("from_%d_to_%d.sql", i, (i + 1));
                Log.d(TAG, "Looking for migration file: " + migrationName);
                readAndExecuteSQLScript(db, context, migrationName);
            }
        } catch (Exception exception) {
            Log.e(TAG, "Exception running upgrade script:", exception);
        }

    }

    /* General Insert function which takes Database object, Table Name and it's Contents then inserts it into local db
     * it's a boolean function so it returns false if data is not inserted
     * */
    public boolean insertData(SQLiteDatabase db, String table, HashMap contents) {
        //Inserts Data in the Specific Table
        //SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //System.out.println("Key: "+me.getKey() + " & Value: " + me.getValue());
        //Map<Integer, Integer> me = contents;
        for (Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>) contents.entrySet())
            contentValues.put(entry.getKey(), entry.getValue());
        long result = db.insert(table, null, contentValues);
        return result != -1;
    }

    private void readAndExecuteSQLScript(SQLiteDatabase db, Context ctx, String fileName) {

        if (TextUtils.isEmpty(fileName)) {
            Log.d(TAG, "SQL script file name is empty");
            return;
        }

        Log.d(TAG, "Script found. Executing...");
        AssetManager assetManager = ctx.getAssets();
        BufferedReader reader = null;

        try {
            InputStream is = assetManager.open(fileName);
            InputStreamReader isr = new InputStreamReader(is);
            reader = new BufferedReader(isr);
            executeSQLScript(db, reader);
        } catch (IOException e) {
            Log.e(TAG, "IOException:", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "IOException:", e);
                }
            }
        }
    }

    private void executeSQLScript(SQLiteDatabase db, BufferedReader reader) throws IOException {
        String line;
        StringBuilder statement = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            statement.append(line);
            statement.append("\n");
            if (line.endsWith(";")) {
                db.execSQL(statement.toString());
                Log.d(TAG, "executeSQLScript: Performed Execution of Script");
                statement = new StringBuilder();
            }
        }
    }

}