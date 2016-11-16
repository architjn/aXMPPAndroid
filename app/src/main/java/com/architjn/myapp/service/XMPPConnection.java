package com.architjn.myapp.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.architjn.myapp.model.PhoneContactInfo;
import com.architjn.myapp.ui.activity.ContactsActivity;
import com.architjn.myapp.ui.activity.ProfileSetupActivity;
import com.architjn.myapp.utils.ContactsUtils;
import com.architjn.myapp.utils.PreferenceUtils;
import com.architjn.myapp.xmpp.SmackInvocationException;
import com.architjn.myapp.xmpp.XMPPHelper;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by architjn on 10/10/2016.
 */

public class XMPPConnection extends Service {

    public static final String ACTION_LOGIN = "action_login";
    public static final String ACTION_SIGNUP = "action_signup";
    public static final String ACTION_CONNECT = "action_connect";
    public static final String ACTION_PROFILE = "action_profile";
    public static final String ACTION_UPDATE_CONTACTS = "action_update_contacts";
    public static Bitmap userPhoto = null;

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
                case ACTION_CONNECT:
                    new AsyncTask<String, Void, String>() {
                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);
                        }

                        @Override
                        protected String doInBackground(String... strings) {
                            try {
                                XMPPHelper.getInstance(context).signupAndLogin(strings[0],
                                        null, null);
                            } catch (SmackInvocationException e) {
                                Log.v("XMPPConnection", e.getMessage());
                                e.printStackTrace();
                            }
                            return null;
                        }
                    }.execute(PreferenceUtils.getField(context, PreferenceUtils.USER));
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
                                XMPPHelper.getInstance(context).signupAndLogin(strings[0],
                                        strings[1], strings[2]);
                            } catch (SmackInvocationException e) {
                                Log.v("XMPPConnection", e.getMessage());
                                e.printStackTrace();
                            }
                            return null;
                        }
                    }.execute(intent.getStringExtra("phno"),
                            intent.getStringExtra("country_code"),
                            intent.getStringExtra("country"));
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
                                if (userPhoto != null) {
                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    userPhoto.compress(Bitmap.CompressFormat.PNG, 50, stream);
                                    Log.v("photo-1", stream.toByteArray().toString());
                                    XMPPHelper.getInstance(context).updateProfile(strings[0],
                                            strings[1],
                                            stream.toByteArray());
                                } else {
                                    XMPPHelper.getInstance(context).updateProfile(strings[0],
                                            strings[1], null);
                                }
                            } catch (SmackInvocationException e) {
                                XMPPHelper.getInstance(context).setState(XMPPHelper.State.DISCONNECTED);
                                Log.v("XMPPConnection", e.getMessage());
                                e.printStackTrace();
                            }
                            return null;
                        }
                    }.execute(intent.getStringExtra("name"), intent.getStringExtra("username"));
                    break;
                case ACTION_UPDATE_CONTACTS:
                    ContactsUtils.loadAllContacts(context, new ContactsUtils.OnLoadFinished() {
                        @Override
                        public void finished() {
                            sendBroadcast(new Intent(ContactsActivity.CONTACTS_UPDATED));
                        }
                    });/*
                    new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            sendBroadcast(new Intent(ContactsActivity.CONTACTS_UPDATED));
                        }

                        @Override
                        protected Void doInBackground(Void... voids) {
                            try {
                                ArrayList<PhoneContactInfo> allContacts = ContactsUtils.getAllPhoneContacts(context);
//                                ContactsUtils.getAllUserContacts(context, allContacts, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    }.execute();*/
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
        filter.addAction(ACTION_CONNECT);
        filter.addAction(ACTION_PROFILE);
        filter.addAction(ACTION_UPDATE_CONTACTS);
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
