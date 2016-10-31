package com.architjn.myapp.model;

/**
 * Created by architjn on 10/30/2016.
 */

public class Chat {
    private final String id;
    private final String contactId;
    private final String updatedOn;
    private final String lastMsg;
    private final boolean isArchived;
    private final boolean isGroup;

    public Chat(String id, String contactId, String updatedOn, String lastMsg,
                int isArchived, int isGroup) {
        this.id = id;
        this.contactId = contactId;
        this.updatedOn = updatedOn;
        this.lastMsg = lastMsg;
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
}
