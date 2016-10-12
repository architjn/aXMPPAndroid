package com.architjn.myapp.utils;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by architjn on 10/12/2016.
 */

public class PreferenceUtils {

    public static String USER = "user";

    public static void updateUser(Context context, String user) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putString(USER, user).apply();
    }

    public static String getField(Context context, String field) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(field, null);
    }

}
