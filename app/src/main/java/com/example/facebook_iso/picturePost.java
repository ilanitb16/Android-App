package com.example.facebook_iso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.Manifest;


import java.io.ByteArrayOutputStream;

public class picturePost extends AppCompatActivity {
    private static final int REQUEST_SELECT_PHOTO = 123;
    private Boolean is_camera;
    private final int CAMERA_REQ_CODE = 100;
    private Uri finalImage;
    private View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_post);

        ImageButton camerabtnPost = findViewById(R.id.camerabtnPost);
        ImageButton btnSelectPhotoPost = findViewById(R.id.btnSelectPhotoPost);
        camerabtnPost.setOnClickListener(v -> {
            is_camera = true;
            CameraPermission();
        });
        btnSelectPhotoPost.setOnClickListener(v -> {
            is_camera = false;
            openGallery();
        });
    }
    private void CameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    CAMERA_REQ_CODE);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        Intent camerai = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camerai, CAMERA_REQ_CODE);
    }


    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_SELECT_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        ImageView profilePic = findViewById(R.id.imageView5);
        if (is_camera) {
            if (resultCode == RESULT_OK) {
                if (requestCode == CAMERA_REQ_CODE) {
                    Bitmap pic = (Bitmap) (data.getExtras().get("data"));
                    finalImage = getImageUri(this, pic);
                }
            }
        } else {
            if (requestCode == REQUEST_SELECT_PHOTO) {
                if (resultCode == RESULT_OK) {
                    Uri photo = data.getData();
                    if (photo != null) {
                        finalImage = photo;
                    } else {
                        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "canceled", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }
        profilePic.setImageURI(finalImage);

        Button done = findViewById(R.id.done);
        done.setOnClickListener(v -> {
            currentPost.getInstance().setImagePost(finalImage);
            finish();
        });
    }

    public Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                bitmap, "Title", null);
        return Uri.parse(path);
    }


}