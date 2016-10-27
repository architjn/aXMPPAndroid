package com.architjn.myapp.utils;

import android.telecom.Call;

/**
 * Created by architjn on 10/27/2016.
 */

public class PermissionUtils {
    public static void checkPermission(String readContacts, Callback callback) {
        callback.onChange(true);
    }

    public interface Callback {
        void onChange(boolean state);
    }
}
