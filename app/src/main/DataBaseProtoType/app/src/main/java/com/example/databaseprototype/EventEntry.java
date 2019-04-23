package com.example.databaseprototype;

import android.provider.BaseColumns;

public class EventEntry implements BaseColumns {
    public static final String TABLE_NAME = "events";
    public static final String COL_EVENT_NAME = "event_name";

    public static final String COL_BATTERY_LEVEL = "battery_level";

    public static final String COL_CALLER_NUMBER = "caller_number";
    //public static final String COL_RATING = "book_rating";
    public static final String COL_CALL_TIME = "call_time";

    public static final String COL_DATE_TIME = "date_time";
    public static final String COL_LOCATION = "location";


    public static final String SQL_CREATE_BOOK_ENTRY_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            EventEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
            EventEntry.COL_EVENT_NAME+ " TEXT ," +
            EventEntry.COL_BATTERY_LEVEL + " INTEGER, " +
            EventEntry.COL_CALLER_NUMBER + " TEXT, " +
            EventEntry.COL_CALL_TIME + " NUMERIC, " +
            EventEntry.COL_LOCATION + " TEXT, " +
            EventEntry.COL_DATE_TIME+ " TEXT)";
}

