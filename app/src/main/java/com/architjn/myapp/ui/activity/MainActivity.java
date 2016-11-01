package com.architjn.myapp.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
        if (PreferenceUtils.getField(this, PreferenceUtils.USER) == null) {
            startActivity(new Intent(this, RegisterActivity.class));
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
}
