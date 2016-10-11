package com.architjn.myapp.xmpp;

import android.content.Context;


import com.architjn.myapp.R;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.packet.VCard;

public class SmackVCardHelper {
    public static final String FIELD_STATUS = "status";

    private Context context;
    private XMPPConnection con;

    public SmackVCardHelper(Context context, XMPPConnection con) {
        this.context = context;
        this.con = con;
    }

    public void save(String nickname, String phno, byte[] avatar) throws SmackInvocationException {
        VCard vCard = new VCard();
        try {
            vCard.setNickName(nickname);
            vCard.setPhoneHome("cell", phno);
            if (avatar != null) {
                vCard.setAvatar(avatar);
            }
            vCard.setField(FIELD_STATUS, context.getString(R.string.default_status));
            vCard.save(con);
        } catch (Exception e) {
            throw new SmackInvocationException(e);
        }
    }

    public void saveStatus(String status) throws SmackInvocationException {
        VCard vCard = loadVCard();
        vCard.setField(FIELD_STATUS, status);

        try {
            vCard.save(con);
        } catch (Exception e) {
            throw new SmackInvocationException(e);
        }
    }

    public String loadStatus() throws SmackInvocationException {
        return loadVCard().getField(FIELD_STATUS);
    }

    public VCard loadVCard(String jid) throws SmackInvocationException {
        VCard vCard = new VCard();
        try {
            vCard.load(con, jid);

            return vCard;
        } catch (Exception e) {
            throw new SmackInvocationException(e);
        }
    }

    public VCard loadVCard() throws SmackInvocationException {
        VCard vCard = new VCard();
        try {
            vCard.load(con);
            return vCard;
        } catch (Exception e) {
            throw new SmackInvocationException(e);
        }
    }
}