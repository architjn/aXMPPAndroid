package com.architjn.myapp.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;

import com.architjn.myapp.R;
import com.architjn.myapp.database.DbHelper;
import com.architjn.myapp.model.Contact;
import com.architjn.myapp.model.PhoneContactInfo;
import com.architjn.myapp.model.UserProfile;
import com.architjn.myapp.ui.activity.InitializationActivity;
import com.architjn.myapp.xmpp.SmackInvocationException;
import com.architjn.myapp.xmpp.XMPPHelper;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by architjn on 10/27/2016.
 */

public class ContactsUtils {

    public static ArrayList<PhoneContactInfo> getAllPhoneContacts(Context context) {
        ArrayList<PhoneContactInfo> arrContacts = new ArrayList<>();
        PhoneContactInfo phoneContactInfo;
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(uri, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone._ID},
                null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (cursor != null && cursor.moveToFirst())
            do {
                String contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                int phoneContactID = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
                if (!isNumberValid(contactNumber))
                    continue;
                phoneContactInfo = new PhoneContactInfo();
                phoneContactInfo.setPhoneContactID(phoneContactID);
                phoneContactInfo.setContactName(contactName);
                phoneContactInfo.setContactNumber(getOnlyNumber(contactNumber));
                arrContacts.add(phoneContactInfo);
                cursor.moveToNext();
            } while (cursor.moveToNext());
        if (cursor != null)
            cursor.close();
        return arrContacts;
    }

    private static boolean isNumberValid(String contactNumber) {
        return !(contactNumber.startsWith("*") || contactNumber.startsWith("#"));
    }

    private static String getOnlyNumber(String contactNumber) {
        try {
            // phone must begin with '+'
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber numberProto = phoneUtil.parse(contactNumber, "");
            int countryCode = numberProto.getCountryCode();
            long nationalNumber = numberProto.getNationalNumber();
            contactNumber = String.valueOf(nationalNumber);
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
            contactNumber = contactNumber.replaceAll("[()\\-\\s]", "").trim();
            if (contactNumber.startsWith("0"))
                contactNumber = contactNumber.substring(1);
        }
        return contactNumber;
    }

    public static void getAllUserContacts(Context context, ArrayList<PhoneContactInfo> allContacts,
                                          InitializationActivity.OnCountUpdate listener) throws Exception {
        int errorCount = 0, count = 0;
        for (PhoneContactInfo info : allContacts) {
            if (listener != null)
                listener.countUpdated(count++, allContacts.size());
            try {
                UserProfile user = XMPPHelper.getInstance(context).search(info.getContactNumber());
                Log.v("aaa", info.getContactName() + " -- " + info.getContactNumber());
                if (user != null) {
                    Log.v("aaa", "passed ^");
                    DbHelper.getInstance(context).updateContact(new Contact(null, user.getUserName(), user.getAvatar(),
                            user.getNickname(), user.getStatus(), null, user.getJid()));
                    if (user.getAvatar() != null)
                        saveUserImage(context, user.getAvatar(), user.getUserName());
                }
                if (allContacts.size() / 2 < errorCount)
                    throw new Exception("Failed Initialization");
            } catch (SmackInvocationException e) {
                e.printStackTrace();
                errorCount++;
            }
        }
    }

    public static void saveUserImage(Context context, byte[] avatar, String phno) {
        Log.d("aaa", "saveUserImage: " + phno);
        File photo = new File(Constants.getProfileThumbFolder(context), phno + ".jpg");
        try {
            FileOutputStream fos = new FileOutputStream(photo.getPath());
            fos.write(avatar);
            fos.close();
        } catch (java.io.IOException e) {
            Log.v("aaaa", phno);
            e.printStackTrace();
        }
    }
}
