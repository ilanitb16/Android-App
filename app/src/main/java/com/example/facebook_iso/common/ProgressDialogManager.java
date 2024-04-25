package com.example.facebook_iso.common;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogManager {
    private static ProgressDialog progressDialog;

    public static void showProgressDialog(Context context, String title, String message) {
        dismissProgressDialog(); // Dismiss any existing dialog
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false); // Set to false if you want to prevent user from dismissing it
        progressDialog.show();
    }

    public static void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
