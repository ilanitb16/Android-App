package com.iso.facebook;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.iso.facebook.auth.LoginScreen;
import com.iso.facebook.common.SharedPreferencesManager;
import com.iso.facebook.common.keys;

public class SplashScreen extends AppCompatActivity
{

    private static final int SPLASH_DELAY = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkUserLoggedIn();
            }
        }, SPLASH_DELAY);
    }

    private void checkUserLoggedIn() {
        boolean isLoggedIn = SharedPreferencesManager.getBoolean(SplashScreen.this, keys.loggedIn, false);
        Intent intent;
        if (isLoggedIn) {
            intent = new Intent(SplashScreen.this, FeedScreen.class);
        } else {
            intent = new Intent(SplashScreen.this, LoginScreen.class);
        }
        startActivity(intent);
        finish();
    }
}