package com.architjn.myapp.xmpp;

import android.content.Context;
import android.util.Log;

import com.architjn.myapp.R;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.packet.VCard;

public class SmackVCardHelper {
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_USERNAME = "username";
    public static final String FIELD_PHONE = "cell";
    public static final String FIELD_COUNTRY_CODE = "country_code";
    public static final String FIELD_COUNTRY = "country";

    private Context context;
    private XMPPConnection con;

    public SmackVCardHelper(Context context, XMPPConnection con) {
        this.context = context;
        this.con = con;
    }

    public void save(String nickname, String username, String phno,
                     String country, String countryCode, byte[] avatar) throws SmackInvocationException {
        save(nickname, username, phno, null, country, countryCode, avatar);
    }

    public void save(String nickname, String username, String phno, String jid,
                     String country, String countryCode, byte[] avatar) throws SmackInvocationException {
        VCard vCard = new VCard();
        try {
            vCard.setNickName(nickname);
            vCard.setPhoneHome(FIELD_PHONE, phno);
            if (country != null)
                vCard.setField(FIELD_COUNTRY, country);
            if (countryCode != null)
                vCard.setField(FIELD_COUNTRY_CODE, countryCode);
            if (username != null)
                vCard.setField(FIELD_USERNAME, username);
            if (jid != null)
                vCard.setJabberId(jid);
            if (avatar != null) {
                vCard.setAvatar(avatar);
            } else
                vCard.setField(FIELD_STATUS, context.getString(R.string.default_status));
            vCard.save(con);
        } catch (Exception e) {
            XMPPHelper.getInstance(context).setState(XMPPHelper.State.DISCONNECTED);
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

    public void save(String phno, String country, String countryCode) throws SmackInvocationException {
        save(null, null, phno, country, countryCode, null);
    }
}