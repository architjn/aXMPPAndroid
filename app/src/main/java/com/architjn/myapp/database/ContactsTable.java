package com.architjn.myapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.architjn.myapp.model.Contact;
import com.architjn.myapp.model.UserProfile;
import com.architjn.myapp.xmpp.SmackInvocationException;
import com.architjn.myapp.xmpp.XMPPHelper;

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
    private static final String JID = "cont_jid";
    private static final String IS_CONTACT = "cont_is_contact";

    public static void onCreate(SQLiteDatabase database) {
        String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NAME + " TEXT," +
                PHONE + " TEXT," +
                DP + " TEXT," +
                STATUS + " TEXT," +
                JID + " TEXT," +
                LAST_SEEN + " TIMESTAMP," +
                IS_CONTACT + " INTEGER DEFAULT 1)";
        database.execSQL(SQL_CREATE_TABLE);
    }

    public static void onUpgrade(SQLiteDatabase database) {
    }

    static void updateWithNewUsers(SQLiteDatabase db, ArrayList<Contact> allUsers, boolean isContact) {
        for (Contact c : allUsers) {
            String id = doesUserExists(db, c.getPhoneNumber());
            if (id == null) {
                addUser(db, c, isContact);
            } else updateUser(db, c, id, isContact);
        }
    }

    private static void updateUser(SQLiteDatabase db, Contact c, String id, boolean isContact) {
        ContentValues values = new ContentValues();
        values.put(NAME, c.getName());
        values.put(STATUS, c.getStatus());
        values.put(PHONE, c.getPhoneNumber());
        values.putNull(DP);
        values.putNull(LAST_SEEN);
        if (isContact)
            values.put(IS_CONTACT, 1);
        db.update(TABLE_NAME, values, ID + " = ?", new String[]{id});
    }

    private static long addUser(SQLiteDatabase db, Contact c, boolean isContact) {
        ContentValues values = new ContentValues();
        values.putNull(ID);
        values.put(NAME, c.getName());
        values.put(PHONE, c.getPhoneNumber());
        values.put(STATUS, c.getStatus());
        values.put(JID, c.getJid());
        values.putNull(DP);
        values.putNull(LAST_SEEN);
        if (isContact)
            values.put(IS_CONTACT, 1);
        else values.put(IS_CONTACT, 0);
        return db.insert(TABLE_NAME, null, values);
    }

    private static String doesUserExists(SQLiteDatabase db, String number) {
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + PHONE + "= ?", new String[]{number});
        if (c.moveToFirst()) return c.getString(0);
        else return null;
    }

    static ArrayList<Contact> loadAllContacts(SQLiteDatabase db) {
        ArrayList<Contact> contacts = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + IS_CONTACT + " = ?", new String[]{"1"});
        if (c.moveToFirst()) {
            do {
                contacts.add(new Contact(c.getString(c.getColumnIndex(ID)),
                        c.getString(c.getColumnIndex(PHONE)), c.getString(c.getColumnIndex(DP)),
                        c.getString(c.getColumnIndex(NAME)), c.getString(c.getColumnIndex(STATUS)),
                        c.getString(c.getColumnIndex(LAST_SEEN)), c.getString(c.getColumnIndex(JID))));
            } while (c.moveToNext());
        }
        return contacts;
    }

    static Contact getUser(SQLiteDatabase db, String id) {
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + ID + " = ?", new String[]{id});
        if (c.moveToFirst())
            return new Contact(c.getString(c.getColumnIndex(ID)),
                    c.getString(c.getColumnIndex(PHONE)), c.getString(c.getColumnIndex(DP)),
                    c.getString(c.getColumnIndex(NAME)), c.getString(c.getColumnIndex(STATUS)),
                    c.getString(c.getColumnIndex(LAST_SEEN)), c.getString(c.getColumnIndex(JID)));
        else return null;
    }

    static void updateWithNewUser(SQLiteDatabase db, Contact c) {
        String id = doesUserExists(db, c.getPhoneNumber());
        if (id == null) {
            addUser(db, c, true);
        } else updateUser(db, c, id, true);
    }

    static Contact getUserWithMobile(Context context, SQLiteDatabase db, String mobile, boolean isContact)
            throws SmackInvocationException {
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + PHONE + " = ?", new String[]{mobile});
        if (c.moveToFirst())
            return new Contact(c.getString(c.getColumnIndex(ID)),
                    c.getString(c.getColumnIndex(PHONE)), c.getString(c.getColumnIndex(DP)),
                    c.getString(c.getColumnIndex(NAME)), c.getString(c.getColumnIndex(STATUS)),
                    c.getString(c.getColumnIndex(LAST_SEEN)), c.getString(c.getColumnIndex(JID)));
        else {
            UserProfile user = XMPPHelper.getInstance(context).search(mobile);
            return getUser(db, String.valueOf(addUser(db, new Contact(null, user.getUserName(),
                    user.getAvatar(), user.getNickname(), user.getStatus(),
                    null, user.getJid()), isContact)));
        }
    }
}
