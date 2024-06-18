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
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.iso.facebook.R;

public class EditBottomSheetDialog extends BottomSheetDialogFragment
{
    private ImageView closeButton;
    private ConstraintLayout deleteButton;
    private ConstraintLayout editButton;
    private View.OnClickListener editClickListener;
    private View.OnClickListener deleteClickListener;

    public EditBottomSheetDialog(View.OnClickListener editClickListener,
                             View.OnClickListener deleteClickListener) {
        this.editClickListener = editClickListener;
        this.deleteClickListener = deleteClickListener;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.edit_bottom_sheet, container, false);

        // Initialize views
        closeButton = v.findViewById(R.id.close);
        deleteButton = v.findViewById(R.id.delete_button);
        editButton = v.findViewById(R.id.edit_button);

        // Set listeners
        closeButton.setOnClickListener(view -> dismiss());
        editButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                editClickListener.onClick(view);
                dismiss();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                deleteClickListener.onClick(view);
                dismiss();
            }
        });

        return v;
    }
}
