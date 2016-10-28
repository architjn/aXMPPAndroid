package com.architjn.myapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.architjn.myapp.model.Contact;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "chats.db";

    private static DbHelper instance;

    public static synchronized DbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DbHelper(context.getApplicationContext());
        }

        return instance;
    }

    private DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        ChatTable.onCreate(db);
        ContactsTable.onCreate(db);
        ConversationTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void updateContacts(ArrayList<Contact> allUsers) {
        ContactsTable.updateWithNewUsers(this.getWritableDatabase(), allUsers);
    }

    public ArrayList<Contact> loadContacts() {
        return ContactsTable.loadAllContacts(this.getWritableDatabase());
    }
}