package com.example.facebook_iso;

import android.app.Application;
import android.content.Context;

import com.example.facebook_iso.entities.User;

public class MyApplication extends Application{
    public static Context context;
    public void onCreate(){
        super.onCreate();
        context = getApplicationContext();
    }

}
