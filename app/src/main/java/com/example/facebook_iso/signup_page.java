package com.example.facebook_iso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facebook_iso.api_manager.api_manager;
import com.example.facebook_iso.api_manager.constants;
import com.example.facebook_iso.common.ProgressDialogManager;
import com.example.facebook_iso.common.ResponseHandler;
import com.example.facebook_iso.common.SharedPreferencesManager;
import com.example.facebook_iso.entities.User;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Future;

import okhttp3.Response;

public class signup_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        TextView firstName = (TextView) findViewById(R.id.firstName);
        TextView lastName = (TextView) findViewById(R.id.lastName);
        TextView username_text = (TextView) findViewById(R.id.username);
        TextView password_text = (TextView) findViewById(R.id.password);
        TextView passwordConfirm = (TextView) findViewById(R.id.passwordConfirm);



        Button btnlogin = findViewById(R.id.btnlogin);
        btnlogin.setOnClickListener(v -> {
            String first= firstName.getText().toString();
            String last= lastName.getText().toString();
            String user= username_text.getText().toString();
            String pass= password_text.getText().toString();
            String confirm= passwordConfirm.getText().toString();

            boolean err = false;
            if (first.isEmpty()) {
                firstName.setError("First name must contain at least one character");
                err = true;
            } else {
                firstName.setError(null);
            }

            if (hasNumber(first)) {
                firstName.setError("Invalid input. Names must contain letters only");
                err = true;
            } else {
                firstName.setError(null);
            }

            if (last.isEmpty()) {
                lastName.setError("Last name cannot be empty");
                err = true;
            } else {
                lastName.setError(null);
            }

            if (hasNumber(last)) {
                lastName.setError("Invalid input. Names must contain letters only");
                err = true;
            } else {
                lastName.setError(null);
            }

            if (user.isEmpty()) {
                username_text.setError("Username is required");
                err = true;
            } else {
                username_text.setError(null);
            }
            if (logininfo_list.getInstance().find_by_user(user)!=null) {
                username_text.setError("Username is alredy exists");
                err = true;
            } else {
                username_text.setError(null);
            }

            if (pass.length() < 8) {
                password_text.setError("Password must be at least 8 characters long");
                err = true;
            } else {
                password_text.setError(null);
            }

            if (!password_valid(pass)) {
                password_text.setError("Password must Have a capital letter and a number");
                err = true;
            } else {
                password_text.setError(null);
            }

            if (!(pass.equals(confirm)) || confirm.isEmpty()) {
                passwordConfirm.setError("Passwords are not matching");
                err = true;
            } else {
                passwordConfirm.setError(null);
            }

            if (!err) {
                Intent i =new Intent(this, picture.class).putExtra("username", user).putExtra("password", pass).putExtra("name", first + " " + last).putExtra("profilePicture", "");
                startActivity(i);
            } else {
                Toast.makeText(this, "Invalid Form", Toast.LENGTH_SHORT).show();
            }
        });

        Button alreadyHave = findViewById(R.id.alreadyHave);
        alreadyHave.setOnClickListener(v -> {
            Intent i =new Intent(this, Login_Page.class);
            startActivity(i);
        });
        Log.i("Main Activity","onCreate");
    }


    public boolean hasNumber(String name) {
        for (char c : name.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }
    public boolean password_valid(String password){
        boolean has_capital= false;
        boolean has_num = hasNumber(password);
        for (char c : password.toCharArray()) {
            if (c>='A'&&c<='Z') {
                has_capital = true;
            }
        }
        return has_capital&&has_num;
    }
}