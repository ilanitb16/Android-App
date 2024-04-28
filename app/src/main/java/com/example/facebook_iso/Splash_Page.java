package com.example.facebook_iso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.example.facebook_iso.common.SharedPreferencesManager;

public class Splash_Page extends AppCompatActivity
{
    private static final int SPLASH_DELAY = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_page);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkUserLoggedIn();
            }
        }, SPLASH_DELAY);
    }

    private void checkUserLoggedIn() {
        boolean isLoggedIn = SharedPreferencesManager.getBoolean(Splash_Page.this, keys.loggedIn, false);
        Intent intent;
        if (isLoggedIn) {
            intent = new Intent(Splash_Page.this, Feed_Page.class);
        } else {
            intent = new Intent(Splash_Page.this, Login_Page.class);
        }
        startActivity(intent);
        finish();
    }
}