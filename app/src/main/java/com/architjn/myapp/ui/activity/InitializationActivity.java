package com.architjn.myapp.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.architjn.myapp.R;
import com.architjn.myapp.database.DbHelper;
import com.architjn.myapp.model.Contact;
import com.architjn.myapp.model.PhoneContactInfo;
import com.architjn.myapp.model.UserProfile;
import com.architjn.myapp.utils.ContactsUtils;
import com.architjn.myapp.utils.PermissionUtils;
import com.architjn.myapp.utils.PreferenceUtils;
import com.architjn.myapp.xmpp.SmackInvocationException;
import com.architjn.myapp.xmpp.XMPPHelper;

import java.util.ArrayList;
import java.util.Locale;

import static com.architjn.myapp.service.XMPPConnection.ACTION_CONNECT;

/**
 * Created by architjn on 11/01/2016.
 */

public class InitializationActivity extends AppCompatActivity {

    private TextView counter;
    private ProgressBar progress;
    private View takesTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        counter = (TextView) findViewById(R.id.counter);
        progress = (ProgressBar) findViewById(R.id.progress);
        takesTime = findViewById(R.id.takes_time);
        final OnCountUpdate listener = new OnCountUpdate() {
            @Override
            public void countUpdated(int pos, int total) {
//                counter.setText(String.format("%d/%d", pos, total));
            }
        };
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                takesTime.setVisibility(View.VISIBLE);
            }
        }, 5000);

        PermissionUtils.checkPermission(Manifest.permission.READ_CONTACTS,
                new PermissionUtils.Callback() {
                    @Override
                    public void onChange(boolean state) {
                        if (state) {
                            canLoadList(listener);
                        } else cannotProceed();
                    }
                });
    }

    private void cannotProceed() {
        Toast.makeText(this, "Need permission to proceed", Toast.LENGTH_LONG).show();
    }

    private void canLoadList(final OnCountUpdate listener) {
        XMPPHelper.getInstance(this).addActionStateChanged(new XMPPHelper.OnStateChange() {
            @Override
            public void stateChanged(XMPPHelper.State state) {
                if (state == XMPPHelper.State.AUTHENTICATED) {
                    new AsyncTask<Void, Void, Integer>() {

                        @Override
                        protected void onPostExecute(Integer res) {
                            super.onPostExecute(res);
                            if (res == 0) {
                                Toast.makeText(InitializationActivity.this, R.string.init_failed,
                                        Toast.LENGTH_LONG).show();
                                return;
                            }
                            if (!isFinishing()) {
                                PreferenceUtils.setRegistrationProcess(InitializationActivity.this, 3);
                                startActivity(new Intent(InitializationActivity.this, MainActivity.class));
                                finish();
                            }
                        }

                        @Override
                        protected Integer doInBackground(Void... voids) {
                            try {
                                ArrayList<PhoneContactInfo> allContacts = ContactsUtils
                                        .getAllPhoneContacts(InitializationActivity.this);
                                if (listener != null)
                                    listener.countUpdated(0, allContacts.size());
                                ContactsUtils.getAllUserContacts(InitializationActivity.this, allContacts, listener);
                            } catch (Exception e) {
                                e.printStackTrace();
                                return 0;
                            }
                            return 1;
                        }
                    }.execute();
                } else if (state == XMPPHelper.State.CONNECTED) {
                    sendBroadcast(new Intent(ACTION_CONNECT));
                }
            }
        });
    }

    public interface OnCountUpdate {
        void countUpdated(int pos, int total);
    }
}
