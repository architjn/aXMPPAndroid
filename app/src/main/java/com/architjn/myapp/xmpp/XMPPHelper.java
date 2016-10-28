package com.architjn.myapp.xmpp;

import android.content.Context;
import android.util.Log;

import com.architjn.myapp.model.UserProfile;
import com.architjn.myapp.utils.Constants;
import com.architjn.myapp.utils.PreferenceUtils;
import com.architjn.myapp.utils.Utils;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.smackx.provider.VCardProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by architjn on 10/10/2016.
 */

public class XMPPHelper {

    public static final String RESOURCE_PART = "Smack";

    private static XMPPHelper instance;
    private static Context context;
    private ArrayList<OnStateChange> listeners;

    private AccountManager accountManager;
    private XMPPConnection conn;
    private SmackVCardHelper vCardHelper;

    private static State state;

    private XMPPHelper(Context context) {
        state = State.DISCONNECTED;
        ProviderManager.getInstance().addIQProvider("vCard", "vcard-temp", new VCardProvider());
        Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.manual);
        listeners = new ArrayList<>();
    }

    public void addActionStateChanged(OnStateChange listener) {
        listeners.add(listener);
    }

    public static XMPPHelper getInstance(Context context) {
        XMPPHelper.context = context;
        if (instance == null)
            instance = new XMPPHelper(context);
        return instance;
    }

    private void createConnection() {
        if (conn != null)
            return;
        Log.v("XMPPHelper", "creating connection");
        ConnectionConfiguration connConfig =
                new ConnectionConfiguration(Constants.SERVERNAME,
                        Constants.PORT, Constants.SERVERNAME);
        XMPPConnection connection = new XMPPConnection(connConfig);

        try {
            connection.connect();
            conn = connection;
        } catch (XMPPException ex) {
            setState(State.DISCONNECTED);
            Log.e("XMPPClient", "Failed to connect to " + connection.getHost());
            Log.e("XMPPClient", ex.toString());
            conn = null;
        }
    }

    public void signupAndLogin(String username, String phno, byte[] avatar) throws SmackInvocationException {
        setState(State.CONNECTING);
        createConnection();

        Map<String, String> attributes = new HashMap<>();
        attributes.put("name", username);
        attributes.put("phno", phno);
        try {
            Log.v("XMPPHelper", "creating account");
            accountManager = new AccountManager(conn);
            accountManager.createAccount(phno, Utils.getPassword(phno), attributes);
            Log.v("XMPPHelper", "created account");
        } catch (Exception e) {
            if (!e.getMessage().startsWith("conflict")) {
                setState(State.DISCONNECTED);
                throw new SmackInvocationException(e);
            } else
                Log.v("XMPPHelper", "account already exists");
        }

        login(phno);
        vCardHelper.save(username, phno, avatar);
    }

    public void login(String username) throws SmackInvocationException {
        createConnection();

        try {
            Log.v("XMPPHelper", "logging in");
            if (!conn.isAuthenticated()) {
                conn.login(username, Utils.getPassword(username), RESOURCE_PART);
                Log.v("XMPPHelper", "loggedin");
                onConnectionEstablished();
            } else Log.v("XMPPHelper", "connection is authenticated");

            PreferenceUtils.updateUser(context, username);

        } catch (Exception e) {
            setState(State.DISCONNECTED);
            e.printStackTrace();
            throw new SmackInvocationException(e);
        }
        setState(State.CONNECTED);
    }

    private void onConnectionEstablished() {
        conn.sendPacket(new Presence(Presence.Type.available));
        vCardHelper = new SmackVCardHelper(context, conn);
        conn.addPacketListener(new PresencePacketListener(context),
                new PacketTypeFilter(Presence.class));
    }

    public UserProfile search(String username) throws SmackInvocationException {
        String name = StringUtils.parseName(username);
        String jid = null;
        if (name == null || name.trim().length() == 0) {
            jid = username + "@" + conn.getServiceName();
        } else {
            jid = StringUtils.parseBareAddress(username);
        }

        if (vCardHelper == null) {
            return null;
        }

        VCard vCard = vCardHelper.loadVCard(jid);
        String nickname = vCard.getNickName();

        return nickname == null ? null : new UserProfile(jid, vCard);
    }

    public void setState(State state) {
        XMPPHelper.state = state;
        for (OnStateChange listener : listeners) {
            if (listener != null)
                listener.stateChanged(state);
        }
    }

    public static State getState() {
        return XMPPHelper.state;
    }

    public enum State {
        DISCONNECTED, CONNECTING, CONNECTED
    }

    public interface OnStateChange {
        void stateChanged(State state);
    }
}
