package com.apps.mysweet.veiw;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apps.mysweet.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnBoard2Fragment extends Fragment {
    TextView textViewSkip;


    public OnBoard2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_on_board2, container, false);



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        createNextButton();
        createTextSkip();
    }


    private void createNextButton() {

        getView().findViewById(R.id.btn_next2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.viewPager.setCurrentItem(2, true);
            }
        });

    }
    private void createTextSkip() {
        textViewSkip = getView().findViewById(R.id.text_view_skip_btn2);
        textViewSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),LoginActivity.class));
            }
        });

    }
}
