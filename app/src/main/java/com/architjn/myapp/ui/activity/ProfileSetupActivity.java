package com.architjn.myapp.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.architjn.myapp.R;
import com.architjn.myapp.service.XMPPConnection;
import com.architjn.myapp.utils.PreferenceUtils;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by architjn on 11/01/2016.
 */

public class ProfileSetupActivity extends AppCompatActivity {

    public static final String PROFILE_UPDATED = "profile_updated";

    private CircleImageView photo;
    private EditText name;
    private Button save;
    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().matches(PROFILE_UPDATED)) {
                PreferenceUtils.setRegistrationProcess(context, 2);
                startActivity(new Intent(context, InitializationActivity.class));
                finish();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);
        init();
    }

    private void init() {
        registerReceiver(br, new IntentFilter(PROFILE_UPDATED));
        photo = (CircleImageView) findViewById(R.id.photo);
        name = (EditText) findViewById(R.id.name);
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(XMPPConnection.ACTION_PROFILE);
                i.putExtra("name", name.getText().toString());
                sendBroadcast(i);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }
}
