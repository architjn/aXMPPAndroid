package com.architjn.myapp.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.architjn.myapp.R;
import com.architjn.myapp.adapter.ConversationAdapter;
import com.architjn.myapp.model.Chat;
import com.architjn.myapp.database.DbHelper;
import com.architjn.myapp.model.Contact;
import com.architjn.myapp.model.UserProfile;
import com.architjn.myapp.service.XMPPConnection;
import com.architjn.myapp.utils.Utils;
import com.architjn.myapp.xmpp.SmackInvocationException;
import com.architjn.myapp.xmpp.XMPPHelper;

import org.jivesoftware.smack.util.StringUtils;

/**
 * Created by architjn on 10/30/2016.
 */

public class ConversationActivity extends AppCompatActivity implements XMPPHelper.OnStateChange {

    private RecyclerView rv;
    public static Contact currentUser;
    private EditText msg;
    private View send;
    private View emoji;
    private ConversationAdapter adapter;
    private Chat chat;
    private LinearLayoutManager layout;
    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().matches(currentUser.getPhoneNumber())) {
                updateList();
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        init();
    }

    private void init() {
        chat = null;
        if (getIntent().hasExtra("id")) {
            currentUser = DbHelper.getInstance(this).getContact(getIntent().getStringExtra("id"));
            chat = DbHelper.getInstance(this).getChatByUserId(currentUser.getId());
        } else if (getIntent().hasExtra("chatId")) {
            Log.v("chatId", getIntent().getStringExtra("chatId"));
            chat = DbHelper.getInstance(this).getChat(getIntent().getStringExtra("chatId"));
            currentUser = DbHelper.getInstance(this).getContact(chat.getContactId());
        }
        registerReceiver(br, new IntentFilter(currentUser.getPhoneNumber()));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Utils.getContactName(this, currentUser.getPhoneNumber()));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ConversationActivity.this, UserProfileActivity.class);
                i.putExtra("id", currentUser.getId());
                startActivity(i);
            }
        });
        rv = (RecyclerView) findViewById(R.id.conv_list);
        layout = new LinearLayoutManager(this);
        rv.setLayoutManager(layout);
        msg = (EditText) findViewById(R.id.msg);
        send = findViewById(R.id.send);
        emoji = findViewById(R.id.emoji);
        XMPPHelper.getInstance(this).addActionStateChanged(this);
        emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        adapter = new ConversationAdapter(this, DbHelper.getInstance(this)
                .getAllConversations(chat.getId()));
        rv.setAdapter(adapter);
        layout.scrollToPosition(adapter.getItemCount() - 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        currentUser = null;
        unregisterReceiver(br);
    }

    @Override
    public void stateChanged(XMPPHelper.State state) {
        if (state == XMPPHelper.State.AUTHENTICATED) {
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!msg.getText().toString().matches("")) {
                        try {
                            XMPPHelper.getInstance(ConversationActivity.this)
                                    .sendChatMessage(currentUser.getJid(), msg.getText().toString(), null);
                            Contact contact = DbHelper.getInstance(ConversationActivity.this)
                                    .getContactWithNumber(StringUtils.parseName(currentUser.getJid()));
                            DbHelper.getInstance(ConversationActivity.this).addConversation(contact,
                                    msg.getText().toString(), true);
                            updateList();
                            msg.setText(null);
                        } catch (SmackInvocationException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else if (state == XMPPHelper.State.DISCONNECTED) {
            sendBroadcast(new Intent(XMPPConnection.ACTION_CONNECT));
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(ConversationActivity.this, R.string.not_connected, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void updateList() {
        adapter.updateItems(DbHelper.getInstance(ConversationActivity.this)
                .getAllConversations(chat.getId()));
        layout.scrollToPosition(adapter.getItemCount() - 1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
