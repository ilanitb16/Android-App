package com.example.facebook_iso.login;

import static com.example.facebook_iso.login.Login_Page.userViewModel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facebook_iso.Converters;
import com.example.facebook_iso.FeedPage;
import com.example.facebook_iso.R;
import com.example.facebook_iso.editHandler.DataSaver;
import com.example.facebook_iso.entities.User;
import com.example.facebook_iso.picture.picture;

public class SignupPage extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (FeedPage.isDarkMode != null && FeedPage.isDarkMode) {
            setContentView(R.layout.signup_page_dark);
        } else {
            setContentView(R.layout.activity_signup_page);
        }

        TextView firstName = findViewById(R.id.firstName);
        TextView lastName = findViewById(R.id.lastName);
        TextView username_text = findViewById(R.id.username);
        TextView password_text = findViewById(R.id.password);
        TextView passwordConfirm = findViewById(R.id.passwordConfirm);

        Button btnImg = findViewById(R.id.btnImg);
        btnImg.setOnClickListener(v -> {
            Intent i =new Intent(this, picture.class);
            startActivity(i);
        });


        Button btnlogin = findViewById(R.id.btnlogin);
        btnlogin.setOnClickListener(v -> {

            String first= firstName.getText().toString();
            String last= lastName.getText().toString();
            String user= username_text.getText().toString();
            String pass= password_text.getText().toString();
            String confirm = passwordConfirm.getText().toString();
            String img = Converters.uriToString(DataSaver.getInstance().getImagePost());





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
                DataSaver.getInstance().setEdit_F();
                User current = new User(user, img, first + " " + last);
                current.setPassword(pass);

                loginInfo currentInfo = new loginInfo(first, last, user, pass);
                currentInfo.setProfilePic(Converters.fromString(img));
                current_user.getInstance().set_current(currentInfo);


                currentInfo.setNew(true);
                current_user.getInstance().set_CurrentUser(current);


                
                FeedPage.owner = current;

                userViewModel.signUp(current);
                if (FeedPage.owner != null){
                    Intent feedPageActivity = new Intent(Login_Page.context, FeedPage.class);
                    Login_Page.context.startActivity(feedPageActivity);
                }


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