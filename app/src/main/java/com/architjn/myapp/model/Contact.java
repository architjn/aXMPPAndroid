package com.architjn.myapp.model;

/**
 * Created by architjn on 10/27/2016.
 */

public class Contact {
    private final String jid;
    private final String userName;
    private final byte[] avatar;
    private final String nickname;
    private final String status;

    public Contact(String jid, String userName, byte[] avatar, String nickname, String status) {
        this.jid = jid;
        this.userName = userName;
        this.avatar = avatar;
        this.nickname = nickname;
        this.status = status;
    }

    public String getJid() {
        return jid;
    }

    public String getPhoneNumber() {
        return userName;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public String getName() {
        return nickname;
    }

    public String getStatus() {
        return status;
    }
}
