package com.example.facebook_iso.picture;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.facebook_iso.R;
import com.example.facebook_iso.editHandler.DataSaver;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

public class picture extends Activity {

    private static final int REQUEST_SELECT_PHOTO = 1;
    private Boolean is_camera;
    private final int CAMERA_REQ_CODE = 100;

    private DataSaver helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        this.helper= DataSaver.getInstance();
        is_camera= false;

        ImageButton camerabtn = findViewById(R.id.camerabtn);
        ImageButton btnSelectPhoto = findViewById(R.id.btnSelectPhoto);

        camerabtn.setOnClickListener(v -> {
            is_camera=true;
            if (checkCameraPermission()) {
                openCamera();
            } else {
                requestCameraPermission();
            }
        });
        btnSelectPhoto.setOnClickListener(v -> {
            is_camera=false;
            selectPhoto(v);
        });
    }


    public void selectPhoto(View view) {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), REQUEST_SELECT_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        ImageView profilePic = findViewById(R.id.imageView5);
        if (is_camera) {
            if (resultCode == RESULT_OK) {
                if (requestCode == CAMERA_REQ_CODE) {
                    Bitmap pic = (Bitmap) (data.getExtras().get("data"));
                    Uri picUri = getImageUri(this, pic);
                    helper.set_pic(picUri);
                    profilePic.setImageURI(picUri);
                }
            }
        }else {
            if (requestCode == REQUEST_SELECT_PHOTO) {
                if (resultCode == RESULT_OK) {
                    Uri photo = data.getData();
                    try {
                        Bitmap pic = decodeUri(photo);
                        photo = getImageUri(this, pic);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error decoding image", Toast.LENGTH_SHORT).show();
                    }

                    if (photo != null) {
                        helper.set_pic(photo);

                        profilePic.setImageURI(photo);
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
        Button finish_reg = findViewById(R.id.finish_reg);
        finish_reg.setOnClickListener(v -> {
            DataSaver.getInstance().setEdit_F();
            finish();
        });
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

    public Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 400;

        int scale = 1;
        while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE) {
            scale *= 2;
        }
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
    }

    public Uri getImageUri(Context context, Bitmap bitmap) {
        int maxWidth = 1200;
        int maxHeight = 1200;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float scaleFactor = Math.min((float) maxWidth / width, (float) maxHeight / height);


        int scaledWidth = Math.round(width * scaleFactor);
        int scaledHeight = Math.round(height * scaleFactor);
        bitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);


        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

}