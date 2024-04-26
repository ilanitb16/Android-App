package com.example.facebook_iso.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.database.CursorWindow;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facebook_iso.AppDB;
import com.example.facebook_iso.Converters;

import com.example.facebook_iso.FeedPage;
import com.example.facebook_iso.MyApplication;
import com.example.facebook_iso.R;
import com.example.facebook_iso.common.ProgressDialogManager;
import com.example.facebook_iso.common.SharedPreferencesManager;
import com.example.facebook_iso.common.UIToast;
import com.example.facebook_iso.common.keys;
import com.example.facebook_iso.viewmodels.UserViewModel;

import java.lang.reflect.Field;

public class Login_Page extends AppCompatActivity {

    public static UserViewModel userViewModel;
    public static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.context = MyApplication.context;
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);


        if (FeedPage.isDarkMode != null) {
            if (FeedPage.isDarkMode) {
                setContentView(R.layout.login_page_dark);
            } else {
                setContentView(R.layout.login_page);
            }
        } else {
            setContentView(R.layout.login_page);
        }


        Button btnsingup = findViewById(R.id.btnsignup);
        btnsingup.setOnClickListener(v -> {
            Intent signupi = new Intent(this, SignupPage.class);
            startActivity(signupi);
        });

        Button btnlogin = findViewById(R.id.btnlogin);
        btnlogin.setOnClickListener(v -> {
            ProgressDialogManager.showProgressDialog(Login_Page.this, "Logging In", "Please wait...");
            TextView username_text = findViewById(R.id.username);
            TextView password_text = findViewById(R.id.password);
            String username = username_text.getText().toString();
            String pass = password_text.getText().toString();
            userViewModel.signIn(username, pass);

       });
    }
}