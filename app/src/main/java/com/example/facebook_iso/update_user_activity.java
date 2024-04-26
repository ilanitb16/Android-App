package com.example.facebook_iso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.facebook_iso.editHandler.DataSaver;
import com.example.facebook_iso.entities.User;
import com.example.facebook_iso.login.Login_Page;
import com.example.facebook_iso.picture.picture;


public class update_user_activity extends AppCompatActivity {
    private User current;
    private DataSaver helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.current = FeedPage.owner;
        this.helper= DataSaver.getInstance();

        setContentView(R.layout.activity_update_user);

        EditText firstName = findViewById(R.id.firstName);
        EditText lastName = findViewById(R.id.lastName);
        EditText passwordText = findViewById(R.id.password);
        EditText passwordConfirm = findViewById(R.id.passwordConfirm);
        Button btnImg = findViewById(R.id.btnImg);

        btnImg.setOnClickListener(v -> {
            helper.set_pic(null);
            Intent i = new Intent(this, picture.class);
            startActivity(i);
        });

        Button btnUpdateAccount = findViewById(R.id.btnUpdateAccount);
        btnUpdateAccount.setOnClickListener(v -> {
            String first = firstName.getText().toString();
            String last = lastName.getText().toString();
            String pass = passwordText.getText().toString();
            String confirm = passwordConfirm.getText().toString();

            if (helper.getImagePost()!=null){
                current.setProfilePic(Converters.uriToString(helper.getImagePost()));
            }
            else {
                helper.set_pic(Converters.fromString(current.getProfilePic()));
            }
            boolean err = false;
            if (!first.isEmpty()) {
                if (hasNumber(first)) {
                    firstName.setError("Invalid input. Names must contain letters only");
                    err = true;
                } else {
                    String[] names = current.getDisplayName().split("\\s+");
                    names[0] = first;
                    String updatedDisplayName = String.join(" ", names);
                    current.setDisplayName(updatedDisplayName);
                    firstName.setError(null);
                }
            } else {
                firstName.setError(null);
            }

            if (!last.isEmpty()) {
                if (hasNumber(last)) {
                    lastName.setError("Invalid input. Names must contain letters only");
                    err = true;
                } else {
                    String[] names = current.getDisplayName().split("\\s+");
                    names[names.length - 1] = last;
                    String updatedDisplayName = String.join(" ", names);
                    current.setDisplayName(updatedDisplayName);

                    lastName.setError(null);
                }
            } else {
                lastName.setError(null);
            }

            if (!pass.isEmpty()) {
                if (pass.length() < 8) {
                    passwordText.setError("Password must be at least 8 characters long");
                    err = true;
                } else if (!password_valid(pass)) {
                    passwordText.setError("Password must have a capital letter and a number");
                    err = true;
                } else if (!(pass.equals(confirm)) || confirm.isEmpty()) {
                    passwordConfirm.setError("Passwords do not match");
                    err = true;
                } else {
                    current.setPassword(pass);
                    passwordText.setError(null);
                    passwordConfirm.setError(null);
                }
            }


            if (!err) {
                FeedPage.userViewModel.updateUser(current);
                finish();
            } else {
                Toast.makeText(this, "Invalid Form", Toast.LENGTH_SHORT).show();
            }
        });
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