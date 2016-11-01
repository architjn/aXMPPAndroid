package com.architjn.myapp.model;

/**
 * Created by architjn on 10/30/2016.
 */

public class Chat {
    private String id;
    private String contactId;
    private String updatedOn;
    private String lastMsg;
    private final int color;
    private boolean isArchived;
    private boolean isGroup;

    public Chat(String id, String contactId, String updatedOn, String lastMsg, int color,
                int isArchived, int isGroup) {
        this.id = id;
        this.contactId = contactId;
        this.updatedOn = updatedOn;
        this.lastMsg = lastMsg;
        this.color = color;
        this.isArchived = isArchived == 1;
        this.isGroup = isGroup == 1;
    }

    public String getId() {
        return id;
    }

    public String getContactId() {
        return contactId;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void update(Chat chat) {
        this.id = chat.id;
        this.contactId = chat.getContactId();
        this.updatedOn = chat.getUpdatedOn();
        this.lastMsg = chat.getLastMsg();
        this.isArchived = chat.isArchived();
        this.isGroup = chat.isGroup();
    }

    public int getColor() {
        return color;
    }
}
