package com.architjn.myapp.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.architjn.myapp.model.Chat;
import com.architjn.myapp.model.Contact;

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
                UPDATED_ON + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                IS_ARCHIVED + " INTEGER DEFAULT 0," +
                IS_GROUP + " INTEGER DEFAULT 0)";
        database.execSQL(SQL_CREATE_TABLE);
    }

    public static void onUpgrade(SQLiteDatabase database) {
    }

    static String getChatId(SQLiteDatabase db, Contact contact) {
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + CONTACT_ID + " = ?", new String[]{contact.getId()});
        if (c.moveToFirst())
            return c.getString(c.getColumnIndex(ID));
        ContentValues values = new ContentValues();
        values.putNull(ID);
        values.put(CONTACT_ID, contact.getId());
        return String.valueOf(db.insert(TABLE_NAME, null, values));
    }

    public static Chat getChat(SQLiteDatabase db, String id) {
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + CONTACT_ID + " = ?", new String[]{id});
        if (c.moveToFirst())
            return new Chat(c.getString(c.getColumnIndex(ID)), c.getString(c.getColumnIndex(CONTACT_ID)),
                    c.getString(c.getColumnIndex(UPDATED_ON)), c.getInt(c.getColumnIndex(IS_ARCHIVED)),
                    c.getInt(c.getColumnIndex(IS_GROUP)));
        ContentValues values = new ContentValues();
        values.putNull(ID);
        values.put(CONTACT_ID, id);
        db.insert(TABLE_NAME, null, values);
        return getChat(db, id);
    }
}
