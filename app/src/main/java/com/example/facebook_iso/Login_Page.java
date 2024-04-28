package com.example.facebook_iso;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.facebook_iso.api_manager.api_manager;
import com.example.facebook_iso.api_manager.constants;
import com.example.facebook_iso.common.ProgressDialogManager;
import com.example.facebook_iso.common.ResponseHandler;
import com.example.facebook_iso.common.SharedPreferencesManager;
import com.example.facebook_iso.common.UIToast;
import com.example.facebook_iso.entities.User;
import com.example.facebook_iso.entities.UserClass;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Future;

import okhttp3.Response;

public class Login_Page extends AppCompatActivity
{
    private logininfo_list list;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        current_user.getInstance().set_current(null);
        list = logininfo_list.getInstance();
        TextView username_text = (TextView) findViewById(R.id.username);
        TextView password_text = (TextView) findViewById(R.id.password);

        Button btnsingup = findViewById(R.id.btnsignup);
        btnsingup.setOnClickListener(v ->
        {
            Intent signupi = new Intent(this, signup_page.class);
            startActivity(signupi);
        });
        Button btnlogin = findViewById(R.id.btnlogin);
        btnlogin.setOnClickListener(v ->
        {
            String username = username_text.getText().toString();
            String pass = password_text.getText().toString();
            if (!username.isEmpty() && !pass.isEmpty())
            {
                loginUser(username, pass);
            }
            else
            {
                Toast.makeText(this, "Plz fill all fields", Toast.LENGTH_SHORT).show();
            }
        });
        Log.i("Main Activity", "onCreate");
    }

    private Boolean check_password(String pass)
    {
        String realPass = current_user.getInstance().get_Current().getPassword();
        return pass.equals(realPass);
    }

    private void loginUser(String username, String password)
    {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("username", username);
        parameters.put("password", password);
        Future<Response> future = new api_manager(Login_Page.this).post(constants.signIn, parameters);
        ProgressDialogManager.showProgressDialog(Login_Page.this, "Logging In", "Please wait...");
        new Thread(() -> {
            try {
                Response response = future.get();
                ProgressDialogManager.dismissProgressDialog();
                User currentUser = ResponseHandler.handleResponse(Login_Page.this, response, constants.signIn, User.class);
                SharedPreferencesManager.setObject(Login_Page.this, keys.currentUser, currentUser);
                SharedPreferencesManager.setBoolean(Login_Page.this, keys.loggedIn, true);
                startActivity(new Intent(Login_Page.this, Feed_Page.class));
                Login_Page.this.finish();
            } catch (Exception e) {
                ProgressDialogManager.dismissProgressDialog();
                Log.d("Debug123: ", Objects.requireNonNull(e.getMessage()));
                e.printStackTrace();
            }
        }).start();
    }
}