package com.architjn.myapp;

import android.app.Application;
import android.content.Intent;

import com.architjn.myapp.service.XMPPConnection;

/**
 * Created by architjn on 10/11/2016.
 */

public class HomeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, XMPPConnection.class));
    }

}
