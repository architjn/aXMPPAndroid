package com.architjn.myapp.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by architjn on 10/27/2016.
 */

public class ChatTable {

    private static final String TABLE_NAME = "Chat";

    private static final String ID = "chat_id";
    private static final String CONTACT_ID = "chat_cont_id";
    private static final String UPDATED_ON = "chat_updated_on";
    private static final String IS_ARCHIVED = "chat_is_archive";
    private static final String IS_GROUP = "chat_is_group";

    public static void onCreate(SQLiteDatabase database) {
        String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CONTACT_ID + " INTEGER," +
                UPDATED_ON + " TIMESTAMP," +
                IS_ARCHIVED + " INTEGER DEFAULT 0," +
                IS_GROUP + " INTEGER DEFAULT 0)";
        database.execSQL(SQL_CREATE_TABLE);
    }

    public static void onUpgrade(SQLiteDatabase database) {
    }

}
