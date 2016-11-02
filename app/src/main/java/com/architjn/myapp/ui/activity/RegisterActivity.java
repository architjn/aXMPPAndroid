package com.architjn.myapp.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.architjn.myapp.R;
import com.architjn.myapp.service.XMPPConnection;
import com.architjn.myapp.utils.Constants;
import com.architjn.myapp.utils.PreferenceUtils;
import com.architjn.myapp.xmpp.XMPPHelper;

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
        final ArrayList<String> countries = new ArrayList<String>();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validFields()) {
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
        if (state == XMPPHelper.State.CONNECTING) {
        } else if (state == XMPPHelper.State.AUTHENTICATED) {
            PreferenceUtils.setRegistrationProcess(this, 1);
            if (!isFinishing()) {
                startActivity(new Intent(this, ProfileSetupActivity.class));
                finish();
            }
        } else if (state == XMPPHelper.State.DISCONNECTED) {
            try {
                login.setClickable(true);
                loading.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
