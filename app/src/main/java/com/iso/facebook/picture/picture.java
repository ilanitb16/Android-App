//package com.iso.facebook.picture;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import com.iso.facebook.R;
//import com.iso.facebook.common.UIToast;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//public class picture extends Activity {
//
//    private static final int REQUEST_SELECT_PHOTO = 1;
//    private final int CAMERA_REQ_CODE = 100;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_picture);
//
//        ImageButton camerabtn = findViewById(R.id.camerabtn);
//        ImageButton btnSelectPhoto = findViewById(R.id.btnSelectPhoto);
//
//        camerabtn.setOnClickListener(v -> {
//            if (checkCameraPermission()) {
//                openCamera();
//            } else {
//                requestCameraPermission();
//            }
//        });
//        btnSelectPhoto.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                selectPhoto();
//            }
//        });
//    }
//
//
//    public void selectPhoto() {
//        Intent i = new Intent();
//        i.setType("image/*");
//        i.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(i, "Select Picture"), REQUEST_SELECT_PHOTO);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//
//        ImageView profilePic = findViewById(R.id.imageView5);
//            if (resultCode == RESULT_OK) {
//                if (requestCode == CAMERA_REQ_CODE) {
//                    if (data != null && data.getExtras() != null && data.getExtras().containsKey("data")){
//                        Bitmap pic = (Bitmap) (data.getExtras().get("data"));
//                        if(pic != null)
//                        {
//                            Uri picUri = decodeUri(this, data.getData());
//                            profilePic.setImageURI(picUri);
//                        }
//                    }
//                    else
//                    {
//                        Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                if (requestCode == REQUEST_SELECT_PHOTO) {
//                    Uri photo = data.getData();
//                    photo = decodeUri(this, photo);
//
//                    if (photo != null) {
//                        profilePic.setImageURI(photo);
//                    } else {
//                        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//            }
//        Button finish_reg = findViewById(R.id.finish_reg);
//        finish_reg.setOnClickListener(v -> {
//            finish();
//        });
//    }
//    private boolean checkCameraPermission() {
//        return ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") == PackageManager.PERMISSION_GRANTED;
//    }
//
//    private void requestCameraPermission() {
//        ActivityCompat.requestPermissions(this, new String[]{"android.permission.CAMERA"}, CAMERA_REQ_CODE);
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == CAMERA_REQ_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                openCamera();
//            } else {
//                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//    private void openCamera() {
//        Intent camerai = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(camerai, CAMERA_REQ_CODE);
//    }
//
//    public Uri decodeUri(Context context, Uri selectedImage) {
//        try
//        {
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
//            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, options);
//
//            final int REQUIRED_SIZE = 400;
//            int scale = 1;
//            while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE) {
//                scale *= 2;
//            }
//
//            options.inJustDecodeBounds = false;
//            options.inSampleSize = scale;
//            Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, options);
//
//            if (bitmap == null) return null;
//
//            File imagesFolder = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "YourAppImages");
//            if (!imagesFolder.exists() && !imagesFolder.mkdirs()) return null;
//
//            String fileName = "image_" + System.currentTimeMillis() + ".jpg";
//            File imageFile = new File(imagesFolder, fileName);
//
//            try (FileOutputStream fos = new FileOutputStream(imageFile)) {
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//            }
//
//            return Uri.fromFile(imageFile);
//        }
//        catch (IOException e)
//        {
//            UIToast.showToast(context, "Error Decoding Image");
//        }
//
//        return selectedImage;
//    }
//}