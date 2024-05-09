package com.iso.facebook.common.BottomSheet;

import android.view.View;
import androidx.fragment.app.FragmentManager;

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

    public static void showEditBottomSheet(FragmentManager fragmentManager,
                                           View.OnClickListener deleteClickListener,
                                       View.OnClickListener editClickListener) {
        EditBottomSheetDialog bottomSheet = new EditBottomSheetDialog(editClickListener, deleteClickListener);
        bottomSheet.show(fragmentManager, "ModalBottomSheet");
    }
}
