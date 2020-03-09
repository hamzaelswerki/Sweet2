package com.apps.mysweet.veiw;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apps.mysweet.R;
import com.apps.mysweet.adapter.TitlesListAdapter;
import com.apps.mysweet.viewmodel.OrderViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class ModalSheetAdressList extends BottomSheetDialogFragment {
    private ListView listView;
    private ArrayList<String> listAdress;
    private TitlesListAdapter adapter;
    private Button btnAdd;
    private Button btnSelect;
    public static OrderViewModel orderViewModel;
    public String addressSelected;
    private BasketFragment basketFragment;

     ModalSheetAdressList(BasketFragment basketFragment){
         this.basketFragment=basketFragment;
     }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_modal_adress_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = view.findViewById(R.id.list_tittles);
        btnAdd = view.findViewById(R.id.btn_add_place);
        btnSelect = view.findViewById(R.id.btn_select_place);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        orderViewModel = ViewModelProviders.of(this).get(OrderViewModel.class);

        createListOfAddress();
        createButtonAddPlace();
        createButtonSelectPlace();
    }

    private void createButtonAddPlace() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MapsSelectPlaceActivity.class);
                startActivity(intent);

            }
        });
    }

    private void createListOfAddress() {
        listAdress = new ArrayList<>();
        orderViewModel.getListTitles().observe(this, new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                listAdress.addAll(strings);
                adapter = new TitlesListAdapter(getActivity(), listAdress);
                adapter.setOnRadioClickListener(new TitlesListAdapter.OnRadioClickListener() {
                    @Override
                    public void onRadioClicked(String address) {
                        Toast.makeText(getContext(), address, Toast.LENGTH_SHORT).show();
                        addressSelected = address;
                    }
                });
                listView.setAdapter(adapter);
            }
        });

    }

    private void createButtonSelectPlace() {
          btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), addressSelected, Toast.LENGTH_SHORT).show();
                  orderViewModel.updateAddress(addressSelected);

                if (isVisible()) {
                    Bundle b=new Bundle();
                    b.putString("Address",addressSelected);
                    basketFragment.setArguments(b);
                   /* Intent intent = new Intent(getContext(), HomeActivity.class);
                    intent.putExtra("IsComesFromAddTitle",true);
                    intent.putExtra("Address",addressSelected);
                    startActivity(intent);*/
                           dismiss();

                }
                          }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
         basketFragment.getOrderAddess();
     }
}
