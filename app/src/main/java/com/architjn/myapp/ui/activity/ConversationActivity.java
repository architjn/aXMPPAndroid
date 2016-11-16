package com.architjn.myapp.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.architjn.myapp.R;
import com.architjn.myapp.adapter.ConversationAdapter;
import com.architjn.myapp.model.Chat;
import com.architjn.myapp.database.DbHelper;
import com.architjn.myapp.model.Contact;
import com.architjn.myapp.service.XMPPConnection;
import com.architjn.myapp.tasks.CopyFile;
import com.architjn.myapp.utils.Constants;
import com.architjn.myapp.utils.Utils;
import com.architjn.myapp.xmpp.SmackInvocationException;
import com.architjn.myapp.xmpp.XMPPHelper;
import com.squareup.picasso.Picasso;

import org.jivesoftware.smack.util.StringUtils;

import java.io.File;

/**
 * Created by architjn on 10/30/2016.
 */

public class ConversationActivity extends AppCompatActivity implements XMPPHelper.OnStateChange {

    private static final int PICK_IMAGE_REQUEST = 142;
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
    private Snackbar snackBar;
    private ImageView back;


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
        back = (ImageView) findViewById(R.id.backHolder);
        rv.setLayoutManager(layout);
        msg = (EditText) findViewById(R.id.msg);
        send = findViewById(R.id.send);
        emoji = findViewById(R.id.emoji);
        loadWallpaper(false);
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

    private void loadWallpaper(boolean clearCache) {
        if (Constants.getWallpaperFile(this).exists()) {
            if (clearCache)
                Picasso.with(this).invalidate(Constants.getWallpaperFile(this));
            Picasso.with(this)
                    .load(Constants.getWallpaperFile(this))
                    .resize(Utils.getScreenWidth(this), Utils.getScreenHeight(this))
                    .centerCrop()
                    .into(back);
        }
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
            if (snackBar != null && snackBar.isShown()) {
                snackBar.dismiss();
                Snackbar.make(send, R.string.reconnecting, Snackbar.LENGTH_SHORT).show();
            }
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
                    snackBar = Snackbar.make(send, R.string.reconnecting, Snackbar.LENGTH_INDEFINITE);
                    snackBar.show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.conversation_activity, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            try {
                Uri uri = data.getData();
                String imagePath = Utils.getPathOfImage(this, uri);
                File source = new File(imagePath);
                File descFolder = new File(Environment.getExternalStorageDirectory() + File.separator +
                        getString(R.string.app_name));
                if (!descFolder.exists())
                    descFolder.mkdirs();
                File des = new File(descFolder, File.separator + "wallpaper.jpg");
                Log.v("aaa", source.getAbsolutePath());
                Log.v("aaa", des.getAbsolutePath());
                new CopyFile(new CopyFile.OnProcessComplete() {
                    @Override
                    public void fileCopied(File des) {
                        loadWallpaper(true);
                    }
                }).execute(source, des);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.wallpaper:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(
                            Intent.createChooser(intent, "Select a File to Upload"),
                            PICK_IMAGE_REQUEST);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(this, "Please install a File Manager.",
                            Toast.LENGTH_SHORT).show();
                    break;
                }
        }
        return super.onOptionsItemSelected(item);
    }
}