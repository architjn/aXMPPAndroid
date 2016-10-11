package com.architjn.myapp.xmpp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.architjn.myapp.utils.Constants;
import com.architjn.myapp.utils.Utils;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.smackx.search.UserSearchManager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by architjn on 10/10/2016.
 */

public class XMPPHelper {

    public static final String ACTION_STATE_CHANGED = "state_changed";

    private static XMPPHelper instance;
    private static Context context;
    private AccountManager accountManager;
    private XMPPConnection conn;
    private SmackVCardHelper vCardHelper;

    private static State state;

    private XMPPHelper(Context context) {
        state = State.DISCONNECTED;
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
        ConnectionConfiguration connConfig =
                new ConnectionConfiguration(Constants.SERVERNAME,
                        Constants.PORT, Constants.SERVERNAME);
        XMPPConnection connection = new XMPPConnection(connConfig);

        try {
            connection.connect();
            Log.i("XMPPClient", "Connected to " + connection.getHost());
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
        Log.v("XMPPHelper", "creating connection");
        createConnection();

        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put("name", username);
        attributes.put("phno", phno);
        try {
            Log.v("XMPPHelper", "creating account");
            accountManager = new AccountManager(conn);
            accountManager.createAccount(phno, "test", attributes);
            Log.v("XMPPHelper", "created account");
        } catch (Exception e) {
            if (!e.getMessage().startsWith("conflict")) {
                setState(State.DISCONNECTED);
                throw new SmackInvocationException(e);
            }
        }

        login(phno);
    }

    public void login(String username) throws SmackInvocationException {
        createConnection();

        try {
            Log.v("XMPPHelper", "logging in");
            if (!conn.isAuthenticated()) {
                conn.login(username, "test");
//                conn.login(username, Utils.getPassword(username));
                Log.v("XMPPHelper", "loggedin");
            } else Log.v("XMPPHelper", "connection not authenticated");

            conn.sendPacket(new Presence(Presence.Type.available));
            vCardHelper = new SmackVCardHelper(context, conn);
            setState(State.CONNECTED);

        } catch (Exception e) {
            e.printStackTrace();
            setState(State.DISCONNECTED);
            throw new SmackInvocationException(e);
        }
    }

    public void setState(State state) {
        XMPPHelper.state = state;
        Intent br = new Intent(ACTION_STATE_CHANGED);
        context.sendBroadcast(br);
    }

    public static State getState() {
        return XMPPHelper.state;
    }

    public enum State {
        DISCONNECTED, CONNECTING, CONNECTED
    }
}
