package com.architjn.myapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.architjn.myapp.model.Chat;
import com.architjn.myapp.model.Contact;
import com.architjn.myapp.model.Conversation;
import com.architjn.myapp.model.UserProfile;
import com.architjn.myapp.xmpp.SmackInvocationException;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "chats.db";

    private static DbHelper instance;
    private static Context context;

    public static synchronized DbHelper getInstance(Context context) {
        DbHelper.context = context;
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

    public void updateContact(ArrayList<Contact> allUsers) {
        ContactsTable.updateWithNewUsers(this.getWritableDatabase(), allUsers, true);
    }

    public void updateContact(Contact user) {
        ContactsTable.updateWithNewUser(this.getWritableDatabase(), user);
    }

    public ArrayList<Contact> loadContacts() {
        return ContactsTable.loadAllContacts(this.getWritableDatabase());
    }

    public Contact getContact(String id) {
        return ContactsTable.getUser(this.getWritableDatabase(), id);
    }

    public void addConversation(UserProfile user, String msg, boolean sent) throws SmackInvocationException {
        ConversationTable.addConversation(context, this.getWritableDatabase(), user, msg, sent);
        Log.v("aaa", user.getNickname() + " -- " + msg);
    }

    public ArrayList<Conversation> getAllConversations(String chatId){
        return ConversationTable.getAllConversation(this.getWritableDatabase(),chatId);
    }

    public Chat getChat(String id) {
        return ChatTable.getChat(this.getWritableDatabase(), id);
    }
}