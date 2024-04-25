package com.example.facebook_iso.common;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class UIToast {
    private static final Handler handler = new Handler(Looper.getMainLooper());

    public static void showToast(final Context context, final String message) {
        handler.post(() -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
    }
}
