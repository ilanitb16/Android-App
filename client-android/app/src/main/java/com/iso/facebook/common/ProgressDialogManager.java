package com.iso.facebook.common;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.iso.facebook.R;

import java.util.Objects;

@SuppressLint("MissingInflatedId")

public class ProgressDialogManager {
    private static Dialog progressDialog;

    public static void showProgressDialog(Context context, String title, String message) {
        dismissProgressDialog(); // Dismiss any existing dialog

        progressDialog = new Dialog(context);
        progressDialog.setCancelable(false); // Set to false if you want to prevent user from dismissing it

        // Inflate custom layout
        View view = LayoutInflater.from(context).inflate(R.layout.loading_indicator, null);
        progressDialog.setContentView(view);

        // Set dialog background transparent
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Set title
        TextView titleTextView = view.findViewById(R.id.titleTextView);
        titleTextView.setText(title);

        // Set message
        TextView messageTextView = view.findViewById(R.id.messageTextView);
        messageTextView.setText(message);

        // Show dialog
        progressDialog.show();

        // Adjust dialog size
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public static void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
