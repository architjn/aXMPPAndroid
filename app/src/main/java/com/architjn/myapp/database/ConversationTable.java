package com.architjn.myapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.architjn.myapp.model.Chat;
import com.architjn.myapp.model.Contact;
import com.architjn.myapp.model.Conversation;
import com.architjn.myapp.model.UserProfile;
import com.architjn.myapp.xmpp.SmackInvocationException;

import java.util.ArrayList;

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

    private static OnContentChanged listener;

    public static void onCreate(SQLiteDatabase database) {
        String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MESSAGE + " TEXT," +
                CHAT_ID + " INTEGER," +
                SENDER_ID + " INTEGER," +
                STARRED + " INTEGER DEFAULT 0," +
                TYPE + " INTEGER )";
        database.execSQL(SQL_CREATE_TABLE);
    }

    public static void onUpgrade(SQLiteDatabase database) {
    }

    public static void setContentChangedListener(OnContentChanged listener1) {
        listener = listener1;
    }

    static void addConversation(Context context, SQLiteDatabase db, UserProfile user, String msg, boolean sent)
            throws SmackInvocationException {
        String chatId = ChatTable.getChatId(db, ContactsTable.getUserWithMobile(context, db, user.getUserName(), false));
        ContentValues values = new ContentValues();
        values.putNull(ID);
        values.put(MESSAGE, msg);
        values.put(SENDER_ID, user.getUserName());
        values.put(CHAT_ID, chatId);
        values.put(STARRED, 0);
        if (sent) values.put(TYPE, 1);
        else values.put(TYPE, 0);
        db.insert(TABLE_NAME, null, values);
        ChatTable.updateTime(db, chatId);
        if (listener != null)
            listener.contentChanged(ChatTable.getChat(db, chatId));
    }

    public static ArrayList<Conversation> getAllConversation(SQLiteDatabase db, String chatId) {
        ArrayList<Conversation> conversations = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + CHAT_ID + " = ?", new String[]{chatId});
        if (c.moveToFirst()) {
            do {
                conversations.add(new Conversation(c.getString(c.getColumnIndex(ID)),
                        c.getString(c.getColumnIndex(MESSAGE)), c.getString(c.getColumnIndex(CHAT_ID)),
                        c.getString(c.getColumnIndex(SENDER_ID)), c.getString(c.getColumnIndex(STARRED)),
                        c.getInt(c.getColumnIndex(TYPE))));
            } while (c.moveToNext());
        }
        return conversations;
    }

    public static String getLastChatMsg(SQLiteDatabase db, String chatId) {
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + CHAT_ID +
                " = ? ORDER BY " + ID + " DESC LIMIT 1", new String[]{chatId});
        if (c.moveToFirst()) {
            return c.getString(c.getColumnIndex(MESSAGE));
        }
        return null;
    }
    public interface OnContentChanged {
        void contentChanged(Chat chat);
    }


}
