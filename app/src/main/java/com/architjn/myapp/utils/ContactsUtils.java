package com.architjn.myapp.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.architjn.myapp.database.DbHelper;
import com.architjn.myapp.model.Contact;
import com.architjn.myapp.model.PhoneContactInfo;
import com.architjn.myapp.model.UserProfile;
import com.architjn.myapp.xmpp.SmackInvocationException;
import com.architjn.myapp.xmpp.XMPPHelper;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.ArrayList;

/**
 * Created by architjn on 10/27/2016.
 */

public class ContactsUtils {

    public static ArrayList<PhoneContactInfo> getAllPhoneContacts(Context context) {
        ArrayList<PhoneContactInfo> arrContacts = new ArrayList<>();
        PhoneContactInfo phoneContactInfo = null;
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(uri, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone._ID},
                null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (cursor != null && cursor.moveToFirst())
            do {
                String contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                int phoneContactID = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
                Log.v("qqq", "checking - " + contactName);
                if (contactNumber.startsWith("*") || contactNumber.startsWith("#") || !isNumberValid(contactNumber))
                    continue;
                Log.v("qqq", "passed - " + contactName);
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
        }
        return contactNumber;
    }

    public static void getAllUserContacts(Context context, ArrayList<PhoneContactInfo> allContacts) {
        for (PhoneContactInfo info : allContacts)
            try {
                UserProfile user = XMPPHelper.getInstance(context).search(info.getContactNumber());
                if (user != null)
                    DbHelper.getInstance(context).updateContact(new Contact(null, user.getUserName(), user.getAvatar(),
                            user.getNickname(), user.getStatus(), null, user.getJid()));
            } catch (SmackInvocationException e) {
                e.printStackTrace();
            }
    }
}
