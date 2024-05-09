package com.iso.facebook.auth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.iso.facebook.FeedScreen;
import com.iso.facebook.R;
import com.iso.facebook.api.ApiService;
import com.iso.facebook.api.endPoints;
import com.iso.facebook.common.AlertDialogHelper;
import com.iso.facebook.common.ProgressDialogManager;
import com.iso.facebook.common.SharedPreferencesManager;
import com.iso.facebook.common.UIToast;
import com.iso.facebook.common.keys;
import com.iso.facebook.convertors.Convertors;
import com.iso.facebook.entities.CurrentUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpScreen extends AppCompatActivity {

    private static final int REQUEST_SELECT_PHOTO = 1;
    private final int CAMERA_REQ_CODE = 100;
    private TextInputEditText etName, etUsername, etPassword, etConfirmPassword;
    private Button btnSignUp;
    private TextView tvLogin;
    private ImageButton btnBack;
    private OnBitmapChangedListener listener;
    private CircleImageView circleImageView;
    private ImageView demoImageview;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);

        etName = findViewById(R.id.Name);
        etUsername = findViewById(R.id.Username);
        etPassword = findViewById(R.id.Password);
        etConfirmPassword = findViewById(R.id.ConfirmPassword);
        btnSignUp = findViewById(R.id.Button);
        tvLogin = findViewById(R.id.tvLogin);
        btnBack = findViewById(R.id.backButton);
        circleImageView = findViewById(R.id.circleImageView);
        demoImageview = findViewById(R.id.demoImageview);

        etName.addTextChangedListener(new InputTextWatcher(etName));
        etUsername.addTextChangedListener(new InputTextWatcher(etUsername));
        etPassword.addTextChangedListener(new InputTextWatcher(etPassword));
        etConfirmPassword.addTextChangedListener(new InputTextWatcher(etConfirmPassword));

        tvLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(validateFields())
                {
                    SignUp(String.valueOf(etUsername.getText()), String.valueOf(etName.getText()), String.valueOf(etPassword.getText()), circleImageView.getDrawable());
                }
            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AlertDialogHelper.showTwoButtonDialog(SignUpScreen.this, "Image Picker", "Add profile image", "Camera", "Gallery", new AlertDialogHelper.TwoButtonDialogListener()
                {
                    @Override
                    public void onPositiveButtonClicked()
                    {
                        if (checkCameraPermission()) {
                            openCamera();
                        } else {
                            requestCameraPermission();
                        }
                    }

                    @Override
                    public void onNegativeButtonClicked()
                    {
                        selectPhoto();
                    }
                });
            }
        });

        setOnBitmapChangedListener(new OnBitmapChangedListener() {
            @Override
            public void onBitmapChanged(Uri imageUri) {
                if(imageUri != null)
                {
                    circleImageView.setImageURI(imageUri);
                    demoImageview.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


    void SignUp(String username, String name, String password, Drawable imageDrawable)
    {
        String profileImage = Convertors.bitmapToBase64(((BitmapDrawable) imageDrawable).getBitmap());
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("username", username);
            parameters.put("displayName", name);
            parameters.put("password", password);
            parameters.put("profilePic", profileImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ProgressDialogManager.showProgressDialog(SignUpScreen.this, "Signing Up", "Please wait...");
        new ApiService(SignUpScreen.this).post(endPoints.signUp, parameters, null, new ApiService.ApiCallback()
        {
            @Override
            public void onSuccess(JSONObject response)
            {
                ProgressDialogManager.dismissProgressDialog();
                startActivity(new Intent(SignUpScreen.this, LoginScreen.class));
                UIToast.showToast(SignUpScreen.this, "User signed up successfully");
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
                UIToast.showToast(SignUpScreen.this, errorMessage);
                ProgressDialogManager.dismissProgressDialog();
            }
        });
    }
    private boolean validateFields() {
        boolean isValid = true;

        String name = etName.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if(circleImageView.getDrawable() == null)
        {
            UIToast.showToast(this, "Plz upload profile image");
            isValid = false;
        }
        if (TextUtils.isEmpty(name)) {
            etName.setError("Name cannot be empty");
            isValid = false;
        } else {
            etName.setError(null);
        }

        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Username cannot be empty");
            isValid = false;
        } else {
            etUsername.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password cannot be empty");
            isValid = false;
        } else {
            etPassword.setError(null);
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError("Confirm Password cannot be empty");
            isValid = false;
        } else if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            isValid = false;
        } else {
            etConfirmPassword.setError(null);
        }

        return isValid;
    }
    private class InputTextWatcher implements TextWatcher
    {
        private final EditText editText;

        private InputTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable editable) {
            if (editText.getId() == R.id.Name) {
                if (editable.toString().isEmpty()) {
                    editText.setError("Name cannot be empty");
                } else {
                    editText.setError(null);
                }
            } else if (editText.getId() == R.id.Username) {
                if (editable.toString().isEmpty()) {
                    editText.setError("Username cannot be empty");
                } else {
                    editText.setError(null);
                }
            } else if (editText.getId() == R.id.Password) {
                if (editable.toString().isEmpty()) {
                    editText.setError("Password cannot be empty");
                } else {
                    editText.setError(null);
                }
            } else if (editText.getId() == R.id.ConfirmPassword) {
                if (!editable.toString().equals(etPassword.getText().toString())) {
                    editText.setError("Passwords do not match");
                } else {
                    editText.setError(null);
                }
            }
        }
    }

    public interface OnBitmapChangedListener {
        void onBitmapChanged(Uri imageUri);
    }
    public void setOnBitmapChangedListener(OnBitmapChangedListener listener) {
        this.listener = listener;
    }

    public void selectPhoto() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), REQUEST_SELECT_PHOTO);
    }

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") == PackageManager.PERMISSION_GRANTED;
    }
    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.CAMERA"}, CAMERA_REQ_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQ_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera() {
        Intent camerai = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camerai, CAMERA_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;

        if (requestCode == CAMERA_REQ_CODE) {
            Bitmap pic = data != null && data.getExtras() != null ? (Bitmap) data.getExtras().get("data") : null;
            if (pic != null) {
                listener.onBitmapChanged(Convertors.bitmapToUri(this, pic));
            } else {
                Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == REQUEST_SELECT_PHOTO) {
            Uri photo = data != null ? data.getData() : null;
            if (photo != null) {
                listener.onBitmapChanged(photo);
            } else {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
