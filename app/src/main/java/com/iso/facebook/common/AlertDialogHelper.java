package com.iso.facebook.common;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.iso.facebook.R;

public class AlertDialogHelper {

    public static void showTwoButtonDialog(Context context, String title, String message, String positiveButtonText, String negativeButtonText, final TwoButtonDialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (listener != null) {
                            listener.onPositiveButtonClicked();
                        }
                    }
                })
                .setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (listener != null) {
                            listener.onNegativeButtonClicked();
                        }
                    }
                })
                .setCancelable(false);

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();

        // Customize the dialog appearance
        customizeAlertDialog(context, alertDialog);
    }

    @SuppressLint("ResourceAsColor")
    private static void customizeAlertDialog(Context context, AlertDialog alertDialog) {
        // Set white background
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.loading_background);

        // Get primary theme color
        int primaryThemeColor = ContextCompat.getColor(context, R.color.primaryColor);

        // Get buttons
        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);

        // Set text color to primary theme color
        positiveButton.setTextColor(primaryThemeColor);
        negativeButton.setTextColor(primaryThemeColor);

        // Create drawable for rounded corners
        Drawable backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.rounded_button_background);

        // Set rounded corner background for buttons
        positiveButton.setBackground(backgroundDrawable);
        negativeButton.setBackground(backgroundDrawable);

        // Set text color for message text view
        TextView messageTextView = alertDialog.findViewById(android.R.id.message);
        if (messageTextView != null) {
            messageTextView.setTextColor(R.color.white);
        }
    }

    public interface TwoButtonDialogListener {
        void onPositiveButtonClicked();

        void onNegativeButtonClicked();
    }
}
