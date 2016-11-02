package com.architjn.myapp.ui.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.architjn.myapp.R;
import com.architjn.myapp.database.DbHelper;
import com.architjn.myapp.model.Contact;
import com.architjn.myapp.model.UserProfile;
import com.architjn.myapp.utils.Constants;
import com.architjn.myapp.utils.ContactsUtils;
import com.architjn.myapp.utils.Utils;
import com.architjn.myapp.xmpp.SmackInvocationException;
import com.architjn.myapp.xmpp.XMPPHelper;
import com.squareup.picasso.Picasso;

import java.io.File;

public class UserProfileActivity extends AppCompatActivity {

    private Contact currentUser;
    private TextView name;
    private TextView status;
    private TextView phno;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        init();
    }

    private void init() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        currentUser = DbHelper.getInstance(this).getContact(getIntent().getStringExtra("id"));
        name = (TextView) findViewById(R.id.name);
        status = (TextView) findViewById(R.id.status);
        phno = (TextView) findViewById(R.id.phno);
        img = (ImageView) findViewById(R.id.img);
        name.setText(Utils.getContactName(this, currentUser.getPhoneNumber()));
        status.setText(currentUser.getStatus());
        phno.setText(currentUser.getPhoneNumber());
        Picasso.with(this).load(new File(Constants.getProfileThumbFolder(this)
                + File.separator
                + currentUser.getPhoneNumber() + ".jpg"))
                .placeholder(R.drawable.user_default)
                .into(img);
        loadImage();
    }

    private void loadImage() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Log.v("aaa", currentUser.getPhoneNumber());
                    UserProfile user = XMPPHelper.getInstance(UserProfileActivity.this).search(currentUser.getPhoneNumber());
                    if (user != null) {
                        DbHelper.getInstance(UserProfileActivity.this)
                                .updateContact(new Contact(null, user.getUserName(), user.getAvatar(),
                                        user.getNickname(), user.getStatus(), null, user.getJid()));
                        if (user.getAvatar() != null) {
                            Log.v("aaa", "img");
                            ContactsUtils.saveUserImage(UserProfileActivity.this, user.getAvatar(), user.getUserName());
                        } else Log.v("aaa", "no img");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
