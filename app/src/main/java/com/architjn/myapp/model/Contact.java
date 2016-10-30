package com.architjn.myapp.model;

/**
 * Created by architjn on 10/27/2016.
 */

public class Contact {
    private String id;
    private final String userName;
    private final byte[] avatarBlob;
    private final String jid;
    private final String nickname;
    private final String avatar;
    private final String status;
    private final String lastSeen;

    public Contact(String id, String userName, byte[] avatar,
                   String nickname, String status, String lastseen, String jid) {
        this.id = id;
        this.userName = userName;
        this.avatarBlob = avatar;
        this.jid = jid;
        this.avatar = null;
        this.nickname = nickname;
        this.status = status;
        this.lastSeen = lastseen;
    }

    public Contact(String id, String userName, String avatar,
                   String nickname, String status, String lastseen, String jid) {
        this.id = id;
        this.userName = userName;
        this.jid = jid;
        this.avatarBlob = null;
        this.avatar = avatar;
        this.nickname = nickname;
        this.status = status;
        this.lastSeen = lastseen;
    }

    public String getPhoneNumber() {
        return userName;
    }

    public byte[] getAvatarBlob() {
        return avatarBlob;
    }

    public String getName() {
        return nickname;
    }

    public String getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getJid() {
        return jid;
    }
}
