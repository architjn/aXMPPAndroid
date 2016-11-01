package com.architjn.myapp.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.architjn.myapp.R;
import com.architjn.myapp.model.PhoneContactInfo;
import com.architjn.myapp.utils.ContactsUtils;
import com.architjn.myapp.utils.PermissionUtils;
import com.architjn.myapp.utils.PreferenceUtils;

import java.util.ArrayList;

/**
 * Created by architjn on 11/01/2016.
 */

public class InitializationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
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
                PreferenceUtils.setRegistrationProcess(InitializationActivity.this, 3);
                startActivity(new Intent(InitializationActivity.this, MainActivity.class));
                finish();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                Log.v("qqq", "checking contacts");
                ArrayList<PhoneContactInfo> allContacts = ContactsUtils.getAllPhoneContacts(InitializationActivity.this);
                ContactsUtils.getAllUserContacts(InitializationActivity.this, allContacts);
                return null;
            }
        }.execute();

    }
}
