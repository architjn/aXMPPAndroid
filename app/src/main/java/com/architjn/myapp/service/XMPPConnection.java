package com.architjn.myapp.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.Preference;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.Log;

import com.architjn.myapp.ui.activity.ProfileSetupActivity;
import com.architjn.myapp.utils.PreferenceUtils;
import com.architjn.myapp.xmpp.SmackInvocationException;
import com.architjn.myapp.xmpp.XMPPHelper;

/**
 * Created by architjn on 10/10/2016.
 */

public class XMPPConnection extends Service {

    public static final String ACTION_LOGIN = "action_login";
    public static final String ACTION_SIGNUP = "action_signup";
    public static final String ACTION_PROFILE = "action_profile";

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            switch (intent.getAction()) {
                case ACTION_LOGIN:
                    new AsyncTask<String, Void, String>() {
                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);
                        }

                        @Override
                        protected String doInBackground(String... strings) {
                            try {
                                XMPPHelper.getInstance(context).login(strings[0]);
                            } catch (SmackInvocationException e) {
                                Log.v("XMPPConnection", e.getMessage());
                                e.printStackTrace();
                            }
                            return null;
                        }
                    }.execute(intent.getStringExtra("phno"));
                    break;
                case ACTION_SIGNUP:
                    new AsyncTask<String, Void, String>() {
                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);
                        }

                        @Override
                        protected String doInBackground(String... strings) {
                            try {
                                XMPPHelper.getInstance(context).signupAndLogin(strings[0]);
                            } catch (SmackInvocationException e) {
                                Log.v("XMPPConnection", e.getMessage());
                                e.printStackTrace();
                            }
                            return null;
                        }
                    }.execute(intent.getStringExtra("phno"));
                    break;
                case ACTION_PROFILE:
                    new AsyncTask<String, Void, String>() {
                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);
                            sendBroadcast(new Intent(ProfileSetupActivity.PROFILE_UPDATED));
                        }

                        @Override
                        protected String doInBackground(String... strings) {
                            try {
                                XMPPHelper.getInstance(context).updateProfile(strings[0], intent.getByteArrayExtra("img"));
                            } catch (SmackInvocationException e) {
                                Log.v("XMPPConnection", e.getMessage());
                                e.printStackTrace();
                            }
                            return null;
                        }
                    }.execute(intent.getStringExtra("name"));
                    break;
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("XMPPConnection", "starting service");
        loginUser();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_LOGIN);
        filter.addAction(ACTION_SIGNUP);
        filter.addAction(ACTION_PROFILE);
        registerReceiver(br, filter);
        return START_STICKY;
    }

    private void loginUser() {
        String username = PreferenceUtils.getField(this, PreferenceUtils.USER);
        if (username != null) {
            new AsyncTask<String, Void, Void>() {
                @Override
                protected Void doInBackground(String... strings) {
                    try {
                        XMPPHelper.getInstance(XMPPConnection.this).login(strings[0]);
                    } catch (SmackInvocationException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute(username);
        }
    }
}
