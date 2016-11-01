package com.architjn.myapp.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.architjn.myapp.model.Chat;
import com.architjn.myapp.model.Contact;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by architjn on 10/27/2016.
 */

public class ChatTable {

    private static final String TABLE_NAME = "Chat";

    private static final String ID = "chat_id";
    private static final String CONTACT_ID = "chat_cont_id";
    private static final String UPDATED_ON = "chat_updated_on";
    private static final String COLOR_SET = "chat_color";
    private static final String IS_ARCHIVED = "chat_is_archive";
    private static final String IS_GROUP = "chat_is_group";

    public static void onCreate(SQLiteDatabase database) {
        String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CONTACT_ID + " INTEGER," +
                COLOR_SET + " INTEGER DEFAULT 0," +
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
        String id = String.valueOf(db.insert(TABLE_NAME, null, values));
        return id;
    }

    static Chat getChatByUserId(SQLiteDatabase db, String id) {
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + CONTACT_ID + " = ?", new String[]{id});
        if (c.moveToFirst()) {
            String lastMsg = ConversationTable.getLastChatMsg(db, c.getString(c.getColumnIndex(ID)));
            return new Chat(c.getString(c.getColumnIndex(ID)), c.getString(c.getColumnIndex(CONTACT_ID)),
                    c.getString(c.getColumnIndex(UPDATED_ON)), lastMsg,
                    c.getInt(c.getColumnIndex(COLOR_SET)),
                    c.getInt(c.getColumnIndex(IS_ARCHIVED)),
                    c.getInt(c.getColumnIndex(IS_GROUP)));
        }
        ContentValues values = new ContentValues();
        values.putNull(ID);
        values.put(CONTACT_ID, id);
        db.insert(TABLE_NAME, null, values);
        return getChatByUserId(db, id);
    }

    static Chat getChat(SQLiteDatabase db, String id) {
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + ID + " = ?", new String[]{id});
        if (c.moveToFirst()) {
            String lastMsg = ConversationTable.getLastChatMsg(db, c.getString(c.getColumnIndex(ID)));
            return new Chat(c.getString(c.getColumnIndex(ID)), c.getString(c.getColumnIndex(CONTACT_ID)),
                    c.getString(c.getColumnIndex(UPDATED_ON)), lastMsg,
                    c.getInt(c.getColumnIndex(COLOR_SET)),
                    c.getInt(c.getColumnIndex(IS_ARCHIVED)),
                    c.getInt(c.getColumnIndex(IS_GROUP)));
        }
        return null;
    }

    static ArrayList<Chat> getAllChats(SQLiteDatabase db) {
        ArrayList<Chat> chats = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + UPDATED_ON + " DESC", null);
        if (c.moveToFirst())
            do {
                String lastMsg = ConversationTable.getLastChatMsg(db, c.getString(c.getColumnIndex(ID)));
                if (lastMsg != null)
                    chats.add(new Chat(c.getString(c.getColumnIndex(ID)), c.getString(c.getColumnIndex(CONTACT_ID)),
                            c.getString(c.getColumnIndex(UPDATED_ON)), lastMsg,
                            c.getInt(c.getColumnIndex(COLOR_SET)),
                            c.getInt(c.getColumnIndex(IS_ARCHIVED)),
                            c.getInt(c.getColumnIndex(IS_GROUP))));
            } while (c.moveToNext());
        return chats;
    }

    static void updateTime(SQLiteDatabase db, String chatId) {
        ContentValues values = new ContentValues();
        Calendar calendar = Calendar.getInstance();
        java.util.Date now = calendar.getTime();
        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
        values.put(UPDATED_ON, currentTimestamp.toString());
        db.update(TABLE_NAME, values, ID + "= ?", new String[]{chatId});
    }

}
