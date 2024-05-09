package com.iso.facebook.convertors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class Convertors
{
    public static Uri bitmapToUri(Context context, Bitmap bitmap)
    {
        if (bitmap == null)
        {
            return null;
        }

        try
        {
            File imagesFolder = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "YourAppImages");
            if (!imagesFolder.exists())
            {
                if (!imagesFolder.mkdirs())
                {
                    return null;
                }
            }

            String fileName = "image_" + System.currentTimeMillis() + ".jpg";
            File imageFile = new File(imagesFolder, fileName);

            try (FileOutputStream fos = new FileOutputStream(imageFile))
            {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            }

            return Uri.fromFile(imageFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static String bitmapToBase64(Bitmap bitmap)
    {
        int maxWidth = 1200;
        int maxHeight = 1200;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float scaleFactor = Math.min((float) maxWidth / width, (float) maxHeight / height);

        int scaledWidth = Math.round(width * scaleFactor);
        int scaledHeight = Math.round(height * scaleFactor);
        bitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Uri base64ToUri(String base64Image, Context context) {
        String imageDataBytes = base64Image.substring(base64Image.indexOf(",") + 1);
        byte[] imageBytes = Base64.decode(imageDataBytes, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        if (bitmap != null) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String fileName = "image_" + timeStamp + ".jpg";
            File imageFile = new File(context.getFilesDir(), fileName);
            try {
                FileOutputStream fos = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
                return Uri.fromFile(imageFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }



    public static String uriToBase64(Uri uri, Context context)
    {
        try
        {
            @SuppressLint("Recycle") InputStream inputStream = context.getContentResolver().openInputStream(uri);
            byte[] bytes;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1)
            {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            bytes = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String[] convertTimestamp(String timestamp)
    {
        LocalDateTime dateTime = LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_DATE_TIME);
        String date = dateTime.format(DateTimeFormatter.ofPattern("d MMMM uuuu"));
        String time = dateTime.format(DateTimeFormatter.ofPattern("h:mm a"));
        return new String[]{date, time};
    }
}
