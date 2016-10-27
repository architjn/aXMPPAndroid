package com.architjn.myapp.ui.activity;

import android.Manifest;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.architjn.myapp.R;
import com.architjn.myapp.database.DbHelper;
import com.architjn.myapp.model.Contact;
import com.architjn.myapp.model.PhoneContactInfo;
import com.architjn.myapp.model.UserProfile;
import com.architjn.myapp.utils.ContactsUtils;
import com.architjn.myapp.utils.PermissionUtils;
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

    private PhoneNumberUtil phoneUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
//        number.replaceAll("[\\D]", "")
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

    }

    private void canLoadList() {
        ArrayList<PhoneContactInfo> allContacts = ContactsUtils.getAllPhoneContacts(this);
        ArrayList<Contact> allUsers = ContactsUtils.getAllUserContacts(this, allContacts);
        DbHelper.getInstance(this).updateContacts(allUsers);
    }


}
