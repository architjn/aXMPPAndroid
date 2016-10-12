package com.architjn.myapp.ui.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.architjn.myapp.R;
import com.architjn.myapp.service.XMPPConnection;
import com.architjn.myapp.xmpp.XMPPHelper;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by architjn on 10/10/2016.
 */

public class RegisterActivity extends AppCompatActivity {

    private EditText name, phno;
    private CircleImageView avatar;
    private Button login;
    private ProgressDialog pd;

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case XMPPHelper.ACTION_STATE_CHANGED:
                    if (XMPPHelper.getState() == XMPPHelper.State.CONNECTING) {
                        pd = new ProgressDialog(context);
                        pd.setMessage("Logging in..");
                        pd.show();
                    } else if (XMPPHelper.getState() == XMPPHelper.State.CONNECTED) {
                        if (pd != null && pd.isShowing())
                            pd.dismiss();
                        if (!isFinishing()) {
                            startActivity(new Intent(context, MainActivity.class));
                            finish();
                        }
                    } else if (XMPPHelper.getState() == XMPPHelper.State.DISCONNECTED) {
                        if (pd != null && pd.isShowing())
                            pd.dismiss();
                        Log.v("RegisterActivity", "Disconnected");
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        registerReceiver(br, new IntentFilter(XMPPHelper.ACTION_STATE_CHANGED));
        avatar = (CircleImageView) findViewById(R.id.avatar);
        name = (EditText) findViewById(R.id.name);
        phno = (EditText) findViewById(R.id.phno);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent broad = new Intent(XMPPConnection.ACTION_SIGNUP);
                broad.putExtra("name", name.getText().toString());
                broad.putExtra("phno", phno.getText().toString());
                sendBroadcast(broad);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }
}
