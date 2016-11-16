package com.architjn.myapp.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.architjn.myapp.R;
import com.architjn.myapp.api.ApiCaller;
import com.architjn.myapp.service.XMPPConnection;
import com.architjn.myapp.utils.Constants;
import com.architjn.myapp.utils.PreferenceUtils;
import com.architjn.myapp.utils.Utils;
import com.architjn.myapp.xmpp.XMPPHelper;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by architjn on 11/01/2016.
 */

public class ProfileSetupActivity extends AppCompatActivity implements XMPPHelper.OnStateChange {

    public static final String PROFILE_UPDATED = "profile_updated";
    private static final int FILE_SELECT_CODE = 32;

    private CircleImageView photo;
    private EditText name, username;
    private ProgressBar progress;
    private Button save;
    private String finalFile;
    private Bitmap userPhoto = null;
    private String usernameT, nameT;
    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().matches(PROFILE_UPDATED)) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                if (userPhoto != null) {
                    userPhoto.compress(Bitmap.CompressFormat.PNG, 50, stream);
                    updateProfile(context, stream.toByteArray());
                } else
                    updateProfile(context, null);
            }
        }
    };

    private void updateProfile(final Context context, final byte[] bytes) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                PreferenceUtils.setRegistrationProcess(context, 2);
                startActivity(new Intent(context, InitializationActivity.class));
                finish();
            }

            @Override
            protected String doInBackground(String... strings) {
                return ApiCaller.getCaller().updateProfile(strings[0], strings[1], strings[2], strings[3], strings[4], bytes);
            }
        }.execute(PreferenceUtils.getField(ProfileSetupActivity.this, PreferenceUtils.COUNTRY_CODE),
                PreferenceUtils.getField(ProfileSetupActivity.this, PreferenceUtils.USER),
                "Hi", nameT, usernameT);
    }

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
        username = (EditText) findViewById(R.id.username);
        save = (Button) findViewById(R.id.save);
        progress = (ProgressBar) findViewById(R.id.progress);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernameT = username.getText().toString();
                nameT = name.getText().toString();
                if (isValid()) {
                    XMPPHelper.getInstance(ProfileSetupActivity.this)
                            .addActionStateChanged(ProfileSetupActivity.this);
                    progress.setVisibility(View.VISIBLE);
                }
            }
        });
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_SELECT_CODE) {
            try {
                Uri uri = data.getData();
                String profilePath = Utils.getPathOfImage(this, uri);
                String storePath = getCacheDir().getAbsolutePath();
                finalFile = new File(storePath, "cropped.jpg").getAbsolutePath();
                if (!new File(storePath).exists())
                    new File(storePath).mkdirs();
                else if (new File(finalFile).exists()) {
                    new File(finalFile).delete();
                }
                if (!new File(Constants.getProfileThumbFolder(this)).exists())
                    new File(Constants.getProfileThumbFolder(this)).mkdirs();
                File finalPath = new File(Constants.getProfileThumbFolder(this));
                Crop.of(Uri.fromFile(new File(profilePath)), Uri.fromFile(new File(finalPath,
                        PreferenceUtils.getField(this, PreferenceUtils.USER) + ".jpg")))
                        .asSquare().start(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == Crop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            File img = new File(Constants.getProfileThumbFolder(this) + File.separator +
                    PreferenceUtils.getField(this, PreferenceUtils.USER) + ".jpg");
            Picasso.with(this).load(img)
                    .into(photo);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            userPhoto = BitmapFactory.decodeFile(img.getAbsolutePath(), options);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }

    @Override
    public void stateChanged(XMPPHelper.State state) {
        if (state == XMPPHelper.State.AUTHENTICATED) {
            Intent i = new Intent(XMPPConnection.ACTION_PROFILE);
            i.putExtra("name", name.getText().toString());
            i.putExtra("username", username.getText().toString());
            if (userPhoto != null) {
                XMPPConnection.userPhoto = userPhoto;
            }
            sendBroadcast(i);
        } else if (state == XMPPHelper.State.DISCONNECTED) {
            Log.v("state: ", "disconnected reconnecting");
            sendBroadcast(new Intent(XMPPConnection.ACTION_CONNECT));
        }
    }

    public boolean isValid() {
        return !username.getText().toString().matches("") && !name.getText().toString().matches("");
    }
}
