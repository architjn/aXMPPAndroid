package com.architjn.myapp.xmpp;

import android.content.Context;
import android.util.Log;

import com.architjn.myapp.utils.Constants;
import com.architjn.myapp.utils.Utils;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by architjn on 10/10/2016.
 */

public class XMPPHelper {

    private static XMPPHelper instance;
    private static Context context;
    private final AccountManager accountManager;
    private XMPPConnection conn;
    private SmackVCardHelper vCardHelper;

    public State state;

    private XMPPHelper(Context context) {
        accountManager = new AccountManager(conn);
        state = State.DISCONNECTED;
    }

    public static XMPPHelper getInstance(Context context) {
        XMPPHelper.context = context;
        if (instance == null)
            instance = new XMPPHelper(context);
        return instance;
    }

    private void createConnection() {
        ConnectionConfiguration connConfig =
                new ConnectionConfiguration(Constants.SERVERNAME,
                        Constants.PORT, Constants.SERVERNAME);
        XMPPConnection connection = new XMPPConnection(connConfig);

        try {
            connection.connect();
            Log.i("XMPPClient", "[SettingsDialog] Connected to " + connection.getHost());
            conn = connection;
        } catch (XMPPException ex) {
            Log.e("XMPPClient", "[SettingsDialog] Failed to connect to " + connection.getHost());
            Log.e("XMPPClient", ex.toString());
            conn = null;
        }
    }

    public void signupAndLogin(String username, String phno, byte[] avatar) throws SmackInvocationException {
        createConnection();

        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put("name", username);
        try {
            accountManager.createAccount(phno, Utils.getPassword(phno), attributes);
        } catch (Exception e) {
            throw new SmackInvocationException(e);
        }

        login(phno);

        vCardHelper.save(username, avatar);
    }

    public void login(String username) throws SmackInvocationException {
        createConnection();

        try {
            if (!conn.isAuthenticated()) {
                conn.login(username, Utils.getPassword(username));
            }

            conn.sendPacket(new Presence(Presence.Type.available));
            vCardHelper = new SmackVCardHelper(context, conn);
            state = State.CONNECTED;

        } catch (Exception e) {
            throw new SmackInvocationException(e);
        }
    }

    public enum State {
        DISCONNECTED, CONNECTED
    }
}
