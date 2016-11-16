package com.architjn.myapp.api;

import android.app.Activity;
import android.content.SharedPreferences;

import com.architjn.myapp.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;


public class ApiCaller {

    private static ApiCaller apiCaller;
    Activity activity;
    SharedPreferences sharedpreferences;
    private SharedPreferences preferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String MyPREF = "MyPrefs";
    // String str_language="hindi";

    private ApiCaller() {
    }

    public static ApiCaller getCaller() {
        if (apiCaller == null)
            apiCaller = new ApiCaller();
        return apiCaller;
    }

    public String signup(String code, String phone, String country, String jid) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("code", code);
            jsonObject.accumulate("phone", phone);
            jsonObject.accumulate("country", country);
            jsonObject.accumulate("jid", jid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ServerInteractor.httpServerPost(jsonObject.toString(),
                Constants.SIGNUP);
    }

    public String updateProfile(String code, String phone, String status, String name, String username, byte[] dp) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("code", code);
            jsonObject.accumulate("phone", phone);
            jsonObject.accumulate("status", status);
            jsonObject.accumulate("name", name);
            jsonObject.accumulate("username", username);
            jsonObject.accumulate("dp", dp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ServerInteractor.httpServerPost(jsonObject.toString(),
                Constants.UPDATE_PROFILE);
    }

    public String loadProfile(String code, String phone) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("code", code);
            jsonObject.accumulate("phone", phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ServerInteractor.httpServerPost(jsonObject.toString(),
                Constants.LOAD_PROFILE);
    }

    public String matchContacts(String contacts) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("contacts", contacts);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ServerInteractor.httpServerPost(jsonObject.toString(),
                Constants.CHECK_CONTACTS);
    }
}
