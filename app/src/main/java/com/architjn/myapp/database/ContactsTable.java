package com.architjn.myapp.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.architjn.myapp.model.Contact;

import java.util.ArrayList;

/**
 * Created by architjn on 10/27/2016.
 */

public class ContactsTable {

    private static final String TABLE_NAME = "Contacts";

    private static final String ID = "cont_id";
    private static final String NAME = "cont_name";
    private static final String PHONE = "cont_phone";
    private static final String DP = "cont_dp";
    private static final String LAST_SEEN = "cont_last_seen";
    private static final String IS_CONTACT = "cont_is_contact";

    public static void onCreate(SQLiteDatabase database) {
        String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NAME + " TEXT," +
                PHONE + " TEXT," +
                DP + " TEXT," +
                LAST_SEEN + " TIMESTAMP," +
                IS_CONTACT + " INTEGER DEFAULT 1)";
        database.execSQL(SQL_CREATE_TABLE);
    }

    public static void onUpgrade(SQLiteDatabase database) {
    }

    static void updateWithNewUsers(SQLiteDatabase db, ArrayList<Contact> allUsers) {
        for (Contact c : allUsers) {
            if (!doesUserExists(db, c.getPhoneNumber())) {
                addUser(c);
            } else updateUser(c);
        }
    }

    private static void updateUser(Contact c) {

    }

    private static void addUser(Contact c) {

    }

    private static boolean doesUserExists(SQLiteDatabase db, String number) {
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " " + PHONE + "= ?", new String[]{number});
        return c.moveToFirst();
    }

}
