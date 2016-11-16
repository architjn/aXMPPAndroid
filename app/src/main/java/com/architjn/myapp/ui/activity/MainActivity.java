package com.architjn.myapp.ui.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.architjn.myapp.ListDividerItemDecoration;
import com.architjn.myapp.R;
import com.architjn.myapp.adapter.ChatAdapter;
import com.architjn.myapp.database.DbHelper;
import com.architjn.myapp.model.Chat;
import com.architjn.myapp.model.Conversation;
import com.architjn.myapp.utils.PreferenceUtils;
import com.architjn.myapp.utils.Utils;
import com.architjn.myapp.xmpp.MessagePacketListener;
import com.architjn.myapp.xmpp.XMPPHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements XMPPHelper.OnStateChange {

    private FloatingActionButton fab;
    private RecyclerView rv;
    private ChatAdapter adapter;
    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().matches(MessagePacketListener.UPDATE_CHAT)) {
                items = DbHelper.getInstance(context).getAllChats();
                adapter.update(items);
            }
        }
    };
    private ArrayList<Chat> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean checkFingerprint = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Fingerprint API only available on from Android 6.0 (M)
            FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            if (!fingerprintManager.isHardwareDetected()) {
                // Device doesn't support fingerprint authentication
            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                // User hasn't enrolled any fingerprints to authenticate with
            } else {
                checkFingerprint = true;
            }
        }
        if (PreferenceUtils.getField(this, PreferenceUtils.USER) == null ||
                PreferenceUtils.getRegistrationProcess(this) != 3) {
            if (PreferenceUtils.getField(this, PreferenceUtils.USER) == null)
                PreferenceUtils.setRegistrationProcess(this, 0);
            int state = PreferenceUtils.getRegistrationProcess(this);
            if (state == 0)
                startActivity(new Intent(this, RegisterActivity.class));
            else if (state == 1)
                startActivity(new Intent(this, ProfileSetupActivity.class));
            else if (state == 2)
                startActivity(new Intent(this, InitializationActivity.class));
            finish();
        } else if (checkFingerprint && !getIntent().hasExtra("checked")) {
            startActivity(new Intent(this, FingerprintActivity.class));
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        XMPPHelper.getInstance(this).addActionStateChanged(this);
        registerReceiver(br, new IntentFilter(MessagePacketListener.UPDATE_CHAT));
        rv = (RecyclerView) findViewById(R.id.chat_list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        items = DbHelper.getInstance(this).getAllChats();
        rv.addItemDecoration(new ListDividerItemDecoration(this, Utils.dpToPx(this, 40)));
        adapter = new ChatAdapter(this, items);
        rv.setAdapter(adapter);
        adapter.setOnClickListener(new ChatAdapter.OnItemSelected() {
            @Override
            public void itemSelected(Chat chat) {
                Intent i = new Intent(MainActivity.this, ConversationActivity.class);
                i.putExtra("chatId", String.valueOf(chat.getId()));
                startActivity(i);
            }
        });
        fab = (FloatingActionButton) findViewById(R.id.addChat);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ContactsActivity.class));
            }
        });
    }

    @Override
    public void stateChanged(XMPPHelper.State state) {
        if (state == XMPPHelper.State.CONNECTED) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }
}
