package com.architjn.myapp.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.architjn.myapp.R;
import com.architjn.myapp.api.ApiCaller;
import com.architjn.myapp.model.UserProfile;
import com.architjn.myapp.service.XMPPConnection;
import com.architjn.myapp.utils.Constants;
import com.architjn.myapp.utils.PreferenceUtils;
import com.architjn.myapp.xmpp.SmackInvocationException;
import com.architjn.myapp.xmpp.XMPPHelper;

import org.jivesoftware.smackx.packet.VCard;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by architjn on 10/10/2016.
 */

public class RegisterActivity extends AppCompatActivity implements XMPPHelper.OnStateChange {

    private EditText phno;
    private Button login;
    private Spinner spinnerCountry;
    private EditText phnCode;
    private View loading;
    private String code, phone, country;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle("");
        XMPPHelper.getInstance(this).addActionStateChanged(this);
        phno = (EditText) findViewById(R.id.phno);
        login = (Button) findViewById(R.id.login);
        loading = findViewById(R.id.loading);
        spinnerCountry = (Spinner) findViewById(R.id.spinner_country);
        phnCode = (EditText) findViewById(R.id.et_mobileNumberCode);
        final ArrayList<String> countries = new ArrayList<>();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validFields()) {
                    login.setClickable(false);
                    code = phnCode.getText().toString();
                    phone = phno.getText().toString();
                    country = countries.get(spinnerCountry.getSelectedItemPosition());
                    loading.setVisibility(View.VISIBLE);
                    Intent broad = new Intent(XMPPConnection.ACTION_SIGNUP);
                    broad.putExtra("country_code", phnCode.getText().toString());
                    broad.putExtra("country", countries.get(spinnerCountry.getSelectedItemPosition()));
                    broad.putExtra("phno", phno.getText().toString());
                    sendBroadcast(broad);
                    login.setClickable(false);
                } else Snackbar.make(login, R.string.fill_all_fields, Snackbar.LENGTH_LONG).show();
            }
        });
        final ArrayList<String> phnocode = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(Constants.COUNTRY_JSON);
            JSONArray country = obj.getJSONArray("countries");
            for (int i = 0; i < country.length(); i++) {
                countries.add(country.getJSONObject(i).getString("name"));
                phnocode.add(country.getJSONObject(i).getString("code"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(adapter);
        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                phnCode.setText(phnocode.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerCountry.setSelection(102);
    }

    private boolean validFields() {
        return !phno.getText().toString().matches("") && !phnCode.getText().toString().matches("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void stateChanged(XMPPHelper.State state) {
        if (state == XMPPHelper.State.AUTHENTICATED) {
            saveUser(PreferenceUtils.getField(RegisterActivity.this, PreferenceUtils.JID));
        } else if (state == XMPPHelper.State.DISCONNECTED) {
            try {
                login.setClickable(true);
                loading.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveUser(final String jid) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                return ApiCaller.getCaller().signup(strings[0], strings[1], strings[2], strings[3]);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    JSONObject res = new JSONObject(s);
                    if (res.getInt("status") == 1) {
                        PreferenceUtils.setRegistrationProcess(RegisterActivity.this, 1);
                        PreferenceUtils.setField(RegisterActivity.this, PreferenceUtils.COUNTRY_CODE, code);
                        if (!isFinishing()) {
                            startActivity(new Intent(RegisterActivity.this, ProfileSetupActivity.class));
                            finish();
                        }
                    } else Toast.makeText(RegisterActivity.this,
                            "Error", Toast.LENGTH_LONG).show();
                    login.setClickable(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                    saveUser(PreferenceUtils.getField(RegisterActivity.this, PreferenceUtils.JID));
                }
            }
        }.execute(code, phone, country, jid);
    }
}
