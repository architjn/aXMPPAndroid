package com.architjn.myapp.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;

import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 * Created by architjn on 10/10/2016.
 */

public class Utils {

    public static String getPassword(String username) {
        byte bytes[] = username.getBytes();
        Checksum checksum = new CRC32();
        checksum.update(bytes, 0, bytes.length);
        long checksumValue = checksum.getValue();
        return String.format("%08X", checksumValue);
    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static String getContactName(Context context, String phno) {
        if (phno.isEmpty())
            return "";
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.
                CONTENT_FILTER_URI, Uri.encode(phno));
        Cursor c = context.getContentResolver().query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME},
                null, null, null);
        if (c.moveToFirst())
            return c.getString(c.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        else return phno;
    }
}
