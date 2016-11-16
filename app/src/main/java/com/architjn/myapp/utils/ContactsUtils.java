package com.architjn.myapp.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.architjn.myapp.R;
import com.architjn.myapp.api.ApiCaller;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

    public static void saveUserImage(final Context context, final byte[] avatar, final String phno) {
        if (avatar == null)
            return;
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Log.d("aaa", "saveUserImage: " + phno);
                File photo = new File(Constants.getProfileThumbFolder(context), phno + ".jpg");
                try {
                    FileOutputStream fos = new FileOutputStream(photo.getPath());
                    fos.write(avatar);
                    fos.close();
                } catch (IOException e) {
                    Log.v("aaaa", phno);
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    public static void loadAllContacts(final Context context, final OnLoadFinished listener) {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSONObject res = null;
                try {
                    res = new JSONObject(s);
                    if (res.getInt("status") == 1) {
                        JSONArray users = res.getJSONArray("users");
                        for (int i = 0; i < users.length(); i++) {
                            JSONObject user = users.getJSONObject(i);
//                            byte[] photo = user.getString("dp").getBytes();
                            DbHelper.getInstance(context).updateContact(new Contact(null,
                                    user.getString("phone"), "",
                                    user.getString("name"), user.getString("status"), null,
                                    user.getString("jid")));
                            loadImage(context, user.getString("phone"));
                        }
                        if (listener != null)
                            listener.finished();
                    } else
                        Toast.makeText(context, R.string.init_failed,
                                Toast.LENGTH_LONG).show();
                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(context, R.string.init_failed,
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    ArrayList<PhoneContactInfo> allContacts = ContactsUtils
                            .getAllPhoneContacts(context);
                                /*if (listener != null)
                                    listener.countUpdated(0, allContacts.size());
                                ContactsUtils.getAllUserContacts(context, allContacts, listener);*/
                    StringBuilder contacts = new StringBuilder();
                    int i = 0;
                    for (PhoneContactInfo con :
                            allContacts) {
                        if (i++ != 0)
                            contacts.append(",");
                        contacts.append(con.getContactNumber());
                    }
                    return ApiCaller.getCaller().matchContacts(contacts.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    private static void loadImage(final Context context, final String phone) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    UserProfile user = XMPPHelper.getInstance(context).search(phone);
                    if (user != null)
                        ContactsUtils.saveUserImage(context,
                                user.getAvatar(),
                                phone);
                } catch (SmackInvocationException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        /**/
    }

    public interface OnLoadFinished {
        void finished();
    }
}
