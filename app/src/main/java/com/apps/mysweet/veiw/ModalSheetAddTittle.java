package com.apps.mysweet.veiw;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.apps.mysweet.R;
import com.apps.mysweet.model.Order;
import com.apps.mysweet.viewmodel.HomeViewModel;
import com.apps.mysweet.viewmodel.OrderViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ModalSheetAddTittle extends BottomSheetDialogFragment {

     Button btnSave;
     EditText titleEditeText;
     OrderViewModel orderViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_modal_sheet_add_title,container,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleEditeText=view.findViewById(R.id.title_edite_txt);
        btnSave=view.findViewById(R.id.save_btn);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        orderViewModel = ViewModelProviders.of(this).get(OrderViewModel.class);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(titleEditeText.getText().toString())){
             orderViewModel.AddTitleToList(titleEditeText.getText().toString());
             //orderViewModel.updateAddress(titleEditeText.getText().toString());
                    Intent intent=new Intent(getContext(),HomeActivity.class);
                      intent.putExtra("IsComesFromAddTitle",true);
                         startActivity(intent);

                }
            }
        });

    }
}
