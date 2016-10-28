package com.architjn.myapp.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class RegisterActivity extends AppCompatActivity implements XMPPHelper.OnStateChange {

    private EditText name, phno;
    private CircleImageView avatar;
    private Button login;
    private ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        XMPPHelper.getInstance(this).addActionStateChanged(this);
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
    }

    @Override
    public void stateChanged(XMPPHelper.State state) {
        if (state == XMPPHelper.State.CONNECTING) {
        } else if (state == XMPPHelper.State.CONNECTED) {
            if (pd != null && pd.isShowing())
                pd.dismiss();
            if (!isFinishing()) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        } else if (state == XMPPHelper.State.DISCONNECTED) {
            if (pd != null && pd.isShowing())
                pd.dismiss();
            Log.v("RegisterActivity", "Disconnected");
        }
    }
}
