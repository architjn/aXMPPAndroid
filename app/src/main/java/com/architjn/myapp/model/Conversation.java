package com.architjn.myapp.model;

/**
 * Created by architjn on 10/30/2016.
 */
public class Conversation {
    private final String id;
    private final String message;
    private final String chatId;
    private final String senderId;
    private final String starred;
    private final String time;
    private boolean sent;

    public Conversation(String id, String message, String chatId, String senderId,
                        String starred,String time, int type) {
        this.id = id;
        this.message = message;
        this.chatId = chatId;
        this.senderId = senderId;
        this.starred = starred;
        this.time = time;
        sent = type != 1;
    }

    public boolean isSent() {
        return sent;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getChatId() {
        return chatId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getStarred() {
        return starred;
    }

    public String getTime() {
        return time;
    }
}
