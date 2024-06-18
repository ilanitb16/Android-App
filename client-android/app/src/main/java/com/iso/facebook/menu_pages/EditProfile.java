package com.iso.facebook.menu_pages;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
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
import static com.iso.facebook.FeedScreen.currentUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EditProfile extends AppCompatActivity
{
    private static final int REQUEST_SELECT_PHOTO = 1;
    private final int CAMERA_REQ_CODE = 100;
    private TextInputEditText etName, etPassword, etConfirmPassword;
    private Button btnUpdate, btnCancel;
    private ImageView profileImageview;
    private ImageButton back_button;
    private EditProfile.OnBitmapChangedListener listener;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        etName = findViewById(R.id.Name);
        etPassword = findViewById(R.id.Password);
        etConfirmPassword = findViewById(R.id.ConfirmPassword);
        back_button = findViewById(R.id.back_button);
        btnUpdate = findViewById(R.id.update_buttom);
        btnCancel = findViewById(R.id.cancel_buttom);
        profileImageview = findViewById(R.id.Profile_Image);
        currentUser = SharedPreferencesManager.getObject(EditProfile.this, keys.currentUser, CurrentUser.class);
        setCurrentUser(currentUser);

        back_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(validateFields())
                {
                    updateProfile(String.valueOf(etName.getText()), String.valueOf(etPassword.getText()), profileImageview.getDrawable());
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
        profileImageview.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AlertDialogHelper.showTwoButtonDialog(EditProfile.this, "Image Picker", "Add profile image", "Camera", "Gallery", new AlertDialogHelper.TwoButtonDialogListener()
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

        setOnBitmapChangedListener(new EditProfile.OnBitmapChangedListener() {
            @Override
            public void onBitmapChanged(Uri imageUri) {
                if(imageUri != null)
                {
                    profileImageview.setImageURI(imageUri);
                }
            }
        });
    }

    void updateProfile(String name, String password, Drawable imageDrawable)
    {
        String profileImage = Convertors.bitmapToBase64(((BitmapDrawable) imageDrawable).getBitmap());
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("displayName", name);
            parameters.put("password", password);
            parameters.put("profilePic", profileImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ProgressDialogManager.showProgressDialog(EditProfile.this, "Updating Profile", "Please wait...");
        new ApiService(EditProfile.this).put(endPoints.updateUser + "/" +currentUser.get_id(), parameters, currentUser.getToken(), new ApiService.ApiCallback()
        {
            @Override
            public void onSuccess(JSONObject response)
            {
                ProgressDialogManager.dismissProgressDialog();
                currentUser.setPassword(password);
                currentUser.setDisplayName(name);
                currentUser.setProfilePic(Convertors.bitmapToUri(EditProfile.this, ((BitmapDrawable) imageDrawable).getBitmap()), EditProfile.this);
                SharedPreferencesManager.setObject(EditProfile.this, keys.currentUser, currentUser);
                setCurrentUser(currentUser);
                UIToast.showToast(EditProfile.this, "Profile Updated");
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
                UIToast.showToast(EditProfile.this, errorMessage);
                ProgressDialogManager.dismissProgressDialog();
            }
        });
    }
    private boolean validateFields() {
        boolean isValid = true;

        String name = etName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Name cannot be empty");
            isValid = false;
        } else {
            etName.setError(null);
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

    void setCurrentUser(CurrentUser currentUser)
    {
        assert currentUser != null;
        etName.setText(currentUser.getDisplayName());
        etPassword.setText(currentUser.getPassword());
        etConfirmPassword.setText(currentUser.getPassword());
        profileImageview.setImageURI(currentUser.getProfilePic(EditProfile.this));
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

    public void selectPhoto() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), REQUEST_SELECT_PHOTO);
    }

    public interface OnBitmapChangedListener {
        void onBitmapChanged(Uri imageUri);
    }
    public void setOnBitmapChangedListener(EditProfile.OnBitmapChangedListener listener) {
        this.listener = listener;
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