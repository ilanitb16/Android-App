package com.iso.facebook.auth;

import static com.iso.facebook.FeedScreen.currentUser;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.iso.facebook.FeedScreen;
import com.iso.facebook.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.iso.facebook.SplashScreen;
import com.iso.facebook.api.ApiService;
import com.iso.facebook.api.endPoints;
import com.iso.facebook.common.ProgressDialogManager;
import com.iso.facebook.common.SharedPreferencesManager;
import com.iso.facebook.common.UIToast;
import com.iso.facebook.common.keys;
import com.iso.facebook.entities.CurrentUser;
import com.iso.facebook.entities.User;
import com.iso.facebook.menu_pages.FriendsPage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LoginScreen extends AppCompatActivity {

    private TextInputEditText tvUsername, tvPassword;
    private Button loginButton;
    private TextView signUpTextView;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        // Initialize variables by finding views by their IDs
        tvUsername = findViewById(R.id.tvUsername);
        tvPassword = findViewById(R.id.tvPassword);
        loginButton = findViewById(R.id.button);
        signUpTextView = findViewById(R.id.tvSignUp);

        // Set up TextChangedListeners for real-time error validation
        tvUsername.addTextChangedListener(new InputTextWatcher(tvUsername));
        tvPassword.addTextChangedListener(new InputTextWatcher(tvPassword));

        tvUsername.setText("ilanitber");
        tvPassword.setText("Ii!123456");
        signUpTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(LoginScreen.this, SignUpScreen.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(validateFields())
                {
                    login(tvUsername.getText().toString(), tvPassword.getText().toString());
                }
            }
        });
    }

    void login(String username, String password)
    {
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("username", username);
            parameters.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ProgressDialogManager.showProgressDialog(LoginScreen.this, "Logging In", "Please wait...");
        new ApiService(LoginScreen.this).post(endPoints.signIn, parameters, null, new ApiService.ApiCallback()
        {
            @Override
            public void onSuccess(JSONObject response)
            {
                getFriends(CurrentUser.fromJson(response.toString()));
            }

            @Override
            public void onSuccess(JSONArray response)
            {
                ProgressDialogManager.dismissProgressDialog();
            }

            @Override
            public void onError(String errorMessage)
            {
                UIToast.showToast(LoginScreen.this, errorMessage);
                ProgressDialogManager.dismissProgressDialog();
            }
        });
    }
    private boolean validateFields() {
        boolean isValid = true;

        String username = Objects.requireNonNull(tvUsername.getText()).toString().trim();
        String password = Objects.requireNonNull(tvPassword.getText()).toString().trim();

        if (TextUtils.isEmpty(username)) {
            tvUsername.setError("Username cannot be empty");
            isValid = false;
        } else {
            tvUsername.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            tvPassword.setError("Password cannot be empty");
            isValid = false;
        } else {
            tvPassword.setError(null);
        }

        return isValid;
    }

    private class InputTextWatcher implements TextWatcher
    {
        private TextInputEditText editText;

        private InputTextWatcher(TextInputEditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable editable) {
            // Validate input and set error if necessary
            if (editText.getId() == R.id.tvUsername) {
                if (editable.toString().isEmpty()) {
                    editText.setError("Username cannot be empty");
                } else {
                    editText.setError(null);
                }
            } else if (editText.getId() == R.id.tvPassword) {
                if (editable.toString().isEmpty()) {
                    editText.setError("Password cannot be empty");
                } else {
                    editText.setError(null);
                }
            }
        }
    }

    void getFriends(CurrentUser currentUser)
    {
        new ApiService(LoginScreen.this).get(endPoints.getUser +currentUser.getUsername(), currentUser.getToken(), new ApiService.ApiCallback()
        {
            @Override
            public void onSuccess(JSONObject response)
            {
                ProgressDialogManager.dismissProgressDialog();
                User user = User.fromJson(String.valueOf(response));
                SharedPreferencesManager.setBoolean(LoginScreen.this, keys.loggedIn, true);
                SharedPreferencesManager.setObject(LoginScreen.this, keys.user, user);
                SharedPreferencesManager.setObject(LoginScreen.this, keys.currentUser, currentUser);
                startActivity(new Intent(LoginScreen.this, FeedScreen.class));
                finish();
            }

            @Override
            public void onSuccess(JSONArray response)
            {
                ProgressDialogManager.dismissProgressDialog();
            }

            @Override
            public void onError(String errorMessage)
            {
                UIToast.showToast(LoginScreen.this, errorMessage);
                ProgressDialogManager.dismissProgressDialog();
            }
        });
    }
}
