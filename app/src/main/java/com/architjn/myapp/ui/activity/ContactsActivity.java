package com.architjn.myapp.ui.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.architjn.myapp.ListDividerItemDecoration;
import com.architjn.myapp.R;
import com.architjn.myapp.adapter.ContactAdapter;
import com.architjn.myapp.database.DbHelper;
import com.architjn.myapp.model.Contact;
import com.architjn.myapp.model.PhoneContactInfo;
import com.architjn.myapp.model.UserProfile;
import com.architjn.myapp.service.XMPPConnection;
import com.architjn.myapp.utils.ContactsUtils;
import com.architjn.myapp.utils.PermissionUtils;
import com.architjn.myapp.utils.Utils;
import com.architjn.myapp.xmpp.SmackInvocationException;
import com.architjn.myapp.xmpp.XMPPHelper;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.ArrayList;

/**
 * Created by architjn on 10/27/2016.
 */

public class ContactsActivity extends AppCompatActivity {

    public static final String CONTACTS_UPDATED = "contacts_updated";
    private RecyclerView rv;
    private ContactAdapter adapter;
    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().matches(CONTACTS_UPDATED)) {
                if (adapter != null)
                    adapter.updateItems(DbHelper.getInstance(ContactsActivity.this)
                            .loadContacts());
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        init();
    }

    private void init() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        registerReceiver(br, new IntentFilter(CONTACTS_UPDATED));
        rv = (RecyclerView) findViewById(R.id.contact_list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new ListDividerItemDecoration(this, Utils.dpToPx(this, 40)));
        adapter = new ContactAdapter(ContactsActivity.this,
                DbHelper.getInstance(ContactsActivity.this)
                        .loadContacts());
        rv.setAdapter(adapter);
        adapter.setOnClickListener(new ContactAdapter.OnItemSelected() {
            @Override
            public void itemSelected(Contact contact) {
                Intent i = new Intent(ContactsActivity.this, ConversationActivity.class);
                i.putExtra("id", contact.getId());
                startActivity(i);
            }
        });
        XMPPHelper.getInstance(this).addActionStateChanged(new XMPPHelper.OnStateChange() {
            @Override
            public void stateChanged(XMPPHelper.State state) {
                if (state == XMPPHelper.State.AUTHENTICATED)
                    PermissionUtils.checkPermission(Manifest.permission.READ_CONTACTS,
                            new PermissionUtils.Callback() {
                                @Override
                                public void onChange(boolean state) {
                                    if (state) {
                                        sendBroadcast(new Intent(XMPPConnection.ACTION_UPDATE_CONTACTS));
                                    } else cannotProceed();
                                }
                            });
                else if (state == XMPPHelper.State.DISCONNECTED)
                    sendBroadcast(new Intent(XMPPConnection.ACTION_CONNECT));
            }
        });
    }

    private void cannotProceed() {
        Toast.makeText(this, "Need permission to proceed", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }
}
