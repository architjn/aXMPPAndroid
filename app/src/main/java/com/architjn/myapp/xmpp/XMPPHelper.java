package com.architjn.myapp.xmpp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.architjn.myapp.model.UserProfile;
import com.architjn.myapp.utils.Constants;
import com.architjn.myapp.utils.PreferenceUtils;
import com.architjn.myapp.utils.Utils;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.PacketExtension;
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

    private XMPPHelper() {
        state = State.DISCONNECTED;
        ProviderManager.getInstance().addIQProvider("vCard", "vcard-temp", new VCardProvider());
        Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.manual);
        listeners = new ArrayList<>();
    }

    public void addActionStateChanged(OnStateChange listener) {
        listeners.add(listener);
        listener.stateChanged(state);
        /*if (state == State.CONNECTING) {
            listener.stateChanged(state);
        } else if (conn == null && state != State.DISCONNECTED)
            setState(State.DISCONNECTED);
        else if (conn != null && conn.isConnected() && !conn.isAuthenticated() && state != State.CONNECTED)
            setState(State.CONNECTED);
        else if (conn != null && conn.isAuthenticated() && state != State.AUTHENTICATED)
            setState(State.AUTHENTICATED);
        else {
            setState(State.DISCONNECTED);
        }*/
    }

    public static XMPPHelper getInstance(Context context) {
        XMPPHelper.context = context;
        if (instance == null)
            instance = new XMPPHelper();
        return instance;
    }

    private void createConnection() {
        if (state != State.DISCONNECTED)
            return;
        setState(State.CONNECTING);
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

    public void signupAndLogin(String phno, String countryCode, String country) throws SmackInvocationException {
        createConnection();

        Map<String, String> attributes = new HashMap<>();
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
        vCardHelper.save(phno, country, countryCode);
    }

    public void login(String username) throws SmackInvocationException {
        createConnection();
        if (conn.isConnected())
            setState(State.CONNECTED);
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
        if (conn.isAuthenticated())
            setState(State.AUTHENTICATED);
    }

    private void onConnectionEstablished() throws Exception {
        conn.sendPacket(new Presence(Presence.Type.available));
        vCardHelper = new SmackVCardHelper(context, conn);
        conn.addPacketListener(new MessagePacketListener(context), new MessageTypeFilter(Message.Type.chat));
        conn.addPacketListener(new PresencePacketListener(context),
                new PacketTypeFilter(Presence.class));
    }

    public void sendChatMessage(String to, String body, PacketExtension packetExtension) throws SmackInvocationException {
        if (to == null) {
            Toast.makeText(context, "User Invalid", Toast.LENGTH_LONG).show();
            return;
        }
        Message message = new Message(to, Message.Type.chat);
        message.setBody(body);
        if (packetExtension != null) {
            message.addExtension(packetExtension);
        }
        try {
            conn.sendPacket(message);
        } catch (Exception e) {
            throw new SmackInvocationException(e);
        }
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
        Log.v("state change : ", state.toString());
        XMPPHelper.state = state;
        for (OnStateChange listener : listeners) {
            if (listener != null)
                listener.stateChanged(state);
        }
    }

    public XMPPConnection getConn() {
        return conn;
    }

    public void updateProfile(String name, String username, byte[] imgs) throws SmackInvocationException {
        vCardHelper.save(name, username, PreferenceUtils.getField(context, PreferenceUtils.USER),
                null, null, imgs);
    }

    public enum State {
        DISCONNECTED, CONNECTING, CONNECTED, AUTHENTICATED
    }

    public interface OnStateChange {
        void stateChanged(State state);
    }
}
