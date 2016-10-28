package com.architjn.myapp.database;

import android.content.ContentValues;
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
    private static final String STATUS = "cont_status";
    private static final String IS_CONTACT = "cont_is_contact";

    public static void onCreate(SQLiteDatabase database) {
        String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NAME + " TEXT," +
                PHONE + " TEXT," +
                DP + " TEXT," +
                STATUS + " TEXT," +
                LAST_SEEN + " TIMESTAMP," +
                IS_CONTACT + " INTEGER DEFAULT 1)";
        database.execSQL(SQL_CREATE_TABLE);
    }

    public static void onUpgrade(SQLiteDatabase database) {
    }

    static void updateWithNewUsers(SQLiteDatabase db, ArrayList<Contact> allUsers) {
        for (Contact c : allUsers) {
            String id = doesUserExists(db, c.getPhoneNumber());
            if (id == null) {
                addUser(db, c);
            } else updateUser(db, c, id);
        }
    }

    private static void updateUser(SQLiteDatabase db, Contact c, String id) {
        ContentValues values = new ContentValues();
        values.put(NAME, c.getName());
        values.put(STATUS, c.getStatus());
        values.put(PHONE, c.getPhoneNumber());
        values.putNull(DP);
        values.putNull(LAST_SEEN);
        db.update(TABLE_NAME, values, ID + " = ?", new String[]{id});
    }

    private static void addUser(SQLiteDatabase db, Contact c) {
        ContentValues values = new ContentValues();
        values.putNull(ID);
        values.put(NAME, c.getName());
        values.put(PHONE, c.getPhoneNumber());
        values.put(STATUS, c.getStatus());
        values.putNull(DP);
        values.putNull(LAST_SEEN);
        db.insert(TABLE_NAME, null, values);
    }

    private static String doesUserExists(SQLiteDatabase db, String number) {
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + PHONE + "= ?", new String[]{number});
        if (c.moveToFirst()) return c.getString(0);
        else return null;
    }

    static ArrayList<Contact> loadAllContacts(SQLiteDatabase db) {
        ArrayList<Contact> contacts = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (c.moveToFirst()) {
            do {
                contacts.add(new Contact(c.getString(c.getColumnIndex(ID)),
                        c.getString(c.getColumnIndex(PHONE)), c.getString(c.getColumnIndex(DP)),
                        c.getString(c.getColumnIndex(NAME)), c.getString(c.getColumnIndex(STATUS)),
                        c.getString(c.getColumnIndex(LAST_SEEN))));
            } while (c.moveToNext());
        }
        return contacts;
    }
}
