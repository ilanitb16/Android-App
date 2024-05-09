package com.iso.facebook.common.BottomSheet;

import android.content.Context;
import android.view.View;
import androidx.fragment.app.FragmentManager;

import com.iso.facebook.common.BottomSheetDialog;

public class BottomSheetUtils {

    public static void showBottomSheet(FragmentManager fragmentManager,
                                       String title, String description,
                                       String leftButton, String rightButton,
                                       int icon,
                                       View.OnClickListener rightButtonClick) {
        BottomSheetDialog bottomSheet = new BottomSheetDialog(
                title,
                description,
                leftButton,
                rightButton,
                icon,
                rightButtonClick
        );
        bottomSheet.show(fragmentManager, "ModalBottomSheet");
    }
}
