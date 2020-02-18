package com.apps.mysweet.veiw;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.apps.mysweet.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ModalSheetAddTittle extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_modal_sheet_add_title,container,false);
    }

}
