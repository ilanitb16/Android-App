package com.iso.facebook.menu_pages;

import static com.iso.facebook.FeedScreen.currentUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.iso.facebook.R;
import com.iso.facebook.api.ApiService;
import com.iso.facebook.api.endPoints;
import com.iso.facebook.auth.LoginScreen;
import com.iso.facebook.common.AlertDialogHelper;
import com.iso.facebook.common.ProgressDialogManager;
import com.iso.facebook.common.UIToast;
import com.iso.facebook.convertors.Convertors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CreatePost extends AppCompatActivity {

    private static final int REQUEST_SELECT_PHOTO = 1;
    private final int CAMERA_REQ_CODE = 100;
    private OnBitmapChangedListener listener;

    private ImageButton backButton;
    private ImageView postImage;
    private EditText titleEditText;
    private EditText descriptionEditText;
    private Button postButton;
    private Button cancelButton;
    private CardView postCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        // Initialize views
        backButton = findViewById(R.id.back_button);
        postImage = findViewById(R.id.post_image);
        titleEditText = findViewById(R.id.Name);
        descriptionEditText = findViewById(R.id.Password);
        postButton = findViewById(R.id.post_button);
        cancelButton = findViewById(R.id.cancel_button);
        postCard = findViewById(R.id.post_card);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateFields())
                {
                    addPost(String.valueOf(titleEditText.getText()), String.valueOf(descriptionEditText.getText()), postImage.getDrawable());
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        postCard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AlertDialogHelper.showTwoButtonDialog(CreatePost.this, "Image Picker", "Add profile image", "Camera", "Gallery", new AlertDialogHelper.TwoButtonDialogListener()
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
                    postImage.setImageURI(imageUri);
                }
            }
        });
    }

    void addPost(String title, String description, Drawable imageDrawable)
    {
        String postImage = Convertors.bitmapToBase64(((BitmapDrawable) imageDrawable).getBitmap());
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("username", currentUser.getUsername());
            parameters.put("title", title);
            parameters.put("description", description);
            parameters.put("profilePic", currentUser.getProfilePicBase64());
            parameters.put("img", postImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ProgressDialogManager.showProgressDialog(CreatePost.this, "Adding new post", "Please wait...");
        new ApiService(CreatePost.this).post(endPoints.createPost + currentUser.getUsername()+ "/posts", parameters, currentUser.getToken(), new ApiService.ApiCallback()
        {
            @Override
            public void onSuccess(JSONObject response)
            {
                ProgressDialogManager.dismissProgressDialog();
                UIToast.showToast(CreatePost.this, "Post added successfully");
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
                UIToast.showToast(CreatePost.this, errorMessage);
                ProgressDialogManager.dismissProgressDialog();
            }
        });
    }
    private boolean validateFields() {
        boolean isValid = true;

        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        if(postImage.getDrawable() == null)
        {
            UIToast.showToast(this, "Plz add post image");
            isValid = false;
        }

        if (TextUtils.isEmpty(title)) {
            titleEditText.setError("Title cannot be empty");
            isValid = false;
        } else {
            titleEditText.setError(null);
        }

        if (TextUtils.isEmpty(description)) {
            descriptionEditText.setText("");
        }
        return isValid;
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
