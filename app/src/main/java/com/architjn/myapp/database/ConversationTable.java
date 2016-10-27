package com.architjn.myapp.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by architjn on 10/27/2016.
 */

public class ConversationTable {

    public enum Type {
        IMAGE, VIDEO, TEXT
    }

    private static final String TABLE_NAME = "Conversation";

    private static final String ID = "conv_id";
    private static final String MESSAGE = "conv_msg";
    private static final String CHAT_ID = "conv_chat_id";
    private static final String SENDER_ID = "conv_sender_id";
    private static final String STARRED = "conv_starred";
    private static final String TYPE = "conv_type";

    public static void onCreate(SQLiteDatabase database) {
        String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MESSAGE + " TEXT," +
                CHAT_ID + " INTEGER," +
                SENDER_ID + " INTEGER," +
                STARRED + " INTEGER DEFAULT 1," +
                TYPE + " TEXT )";
        database.execSQL(SQL_CREATE_TABLE);
    }

    public static void onUpgrade(SQLiteDatabase database) {
    }

}
