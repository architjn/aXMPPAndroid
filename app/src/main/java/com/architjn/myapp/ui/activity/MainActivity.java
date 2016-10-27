package com.architjn.myapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.architjn.myapp.R;
import com.architjn.myapp.utils.PreferenceUtils;
import com.architjn.myapp.xmpp.XMPPHelper;

public class MainActivity extends AppCompatActivity implements XMPPHelper.OnStateChange {

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (PreferenceUtils.getField(this, PreferenceUtils.USER) == null) {
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        XMPPHelper.getInstance(this).addActionStateChanged(this);
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
            /*try {
                UserProfile abc = XMPPHelper.getInstance(this).search("7838864992");
                Log.v("MainActivity", abc.getName() + " <<");
            } catch (SmackInvocationException e) {
                e.printStackTrace();
            }*/
        }
    }
}
