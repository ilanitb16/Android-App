package com.example.facebook_iso;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;

import androidx.room.TypeConverter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Converters {
    @TypeConverter
    public static Uri fromString(String value) {
        return value == null ? null : Uri.parse(value);
    }

    @TypeConverter
    public static String uriToString(Uri uri) {
        return uri == null ? null : uri.toString();
    }

    public static String base64ToString(String base64Img) {
        Context context = MyApplication.context;
        byte[] bytesImg = Base64.decode(base64Img, Base64.DEFAULT);
        Bitmap imgBitmap = BitmapFactory.decodeByteArray(bytesImg, 0, bytesImg.length);

        if (imgBitmap == null) {
            return null;
        }

        String fileName = "image_" + System.currentTimeMillis() + ".jpg"; // Unique filename
        String imagePath = saveBitmapToInternalStorage(context, imgBitmap, fileName);
        if (imagePath == null) {
            return null;
        }

        File imageFile = new File(imagePath);
        return imageFile.getAbsolutePath();
    }

    private static String saveBitmapToInternalStorage(Context context, Bitmap bitmap, String fileName) {
        if (bitmap == null) {
            return null;
        }

        File directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);


        File file = new File(directory, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean deleteImageFromStorage(String imagePath) {
        if (imagePath == null) {
            return false;
        }

        File imageFile = new File(imagePath);

        if (imageFile.exists() && imageFile.isFile()) {
            return imageFile.delete();
        } else {
            return false;
        }
    }
    public static void deleteAllImages() {
        File directory = MyApplication.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }
}
