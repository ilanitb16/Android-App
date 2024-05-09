package com.iso.facebook.common.BottomSheet;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.iso.facebook.R;

public class BottomSheetDialog extends BottomSheetDialogFragment {
    private ImageView imageView;
    private ImageView closeButton;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private Button cancelButton;
    private Button logoutButton;

    private String title;
    private String description;
    private String cancelText;
    private String logoutText;
    private int icon;
    private View.OnClickListener logoutClickListener;

    public BottomSheetDialog(String title, String description, String cancelText, String logoutText, int icon,
                             View.OnClickListener logoutClickListener) {
        this.title = title;
        this.description = description;
        this.cancelText = cancelText;
        this.logoutText = logoutText;
        this.icon = icon;
        this.logoutClickListener = logoutClickListener;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_layout, container, false);
//
//        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(v);
//        bottomSheetBehavior.setDraggable(false);
//        bottomSheetBehavior.setFitToContents(true);

        // Initialize views
        imageView = v.findViewById(R.id.imageView3);
        closeButton = v.findViewById(R.id.close);
        titleTextView = v.findViewById(R.id.bannerTitle);
        descriptionTextView = v.findViewById(R.id.bannerDescription);
        cancelButton = v.findViewById(R.id.update_buttom);
        logoutButton = v.findViewById(R.id.update_bdasuttom);

        // Set values
        titleTextView.setText(title);
        descriptionTextView.setText(description);
        cancelButton.setText(cancelText);
        logoutButton.setText(logoutText);
        imageView.setImageDrawable(getResources().getDrawable(icon));

        // Set listeners
        closeButton.setOnClickListener(view -> dismiss());
        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dismiss();
            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                logoutClickListener.onClick(view);
                dismiss();
            }
        });

        return v;
    }
}
