package com.architjn.myapp.ui.activity;

import android.Manifest;
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

    private RecyclerView rv;
    private ContactAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        init();
    }

    private void init() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        rv = (RecyclerView) findViewById(R.id.contact_list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new ListDividerItemDecoration(this, Utils.dpToPx(this, 35)));
        adapter = new ContactAdapter(ContactsActivity.this,
                DbHelper.getInstance(ContactsActivity.this)
                        .loadContacts());
        rv.setAdapter(adapter);
        PermissionUtils.checkPermission(Manifest.permission.READ_CONTACTS,
                new PermissionUtils.Callback() {
                    @Override
                    public void onChange(boolean state) {
                        if (state) {
                            canLoadList();
                        } else cannotProceed();
                    }
                });
    }

    private void cannotProceed() {
        Toast.makeText(this, "Need permission to proceed", Toast.LENGTH_LONG).show();
    }

    private void canLoadList() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter.updateItems(DbHelper.getInstance(ContactsActivity.this)
                        .loadContacts());
            }

            @Override
            protected Void doInBackground(Void... voids) {
                ArrayList<PhoneContactInfo> allContacts = ContactsUtils.getAllPhoneContacts(ContactsActivity.this);
                ArrayList<Contact> allUsers = ContactsUtils.getAllUserContacts(ContactsActivity.this, allContacts);
                DbHelper.getInstance(ContactsActivity.this).updateContacts(allUsers);
                return null;
            }
        }.execute();

    }


}
