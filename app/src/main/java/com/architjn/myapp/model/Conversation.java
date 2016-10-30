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
    private boolean received;

    public Conversation(String id, String message, String chatId, String senderId, String starred, int type) {
        this.id = id;
        this.message = message;
        this.chatId = chatId;
        this.senderId = senderId;
        this.starred = starred;
        received = type != 1;
    }

    public boolean isReceived() {
        return received;
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
}
