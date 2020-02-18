package com.apps.mysweet.veiw;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.apps.mysweet.R;
import com.apps.mysweet.model.Constants;


public class OnBoard3Fragment extends Fragment {
    TextView tvDetails;
    Button btn_started;

    public OnBoard3Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_on_board3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvDetails = getView().findViewById(R.id.textView);
        btn_started = getView().findViewById(R.id.btn_started);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        makeTxtViewDetailsSpanable();
        createButtonStarted();
    }

    private void makeTxtViewDetailsSpanable() {
        Spannable wordtoSpan = new SpannableString(getResources().getString(R.string.details_onborad3));

        wordtoSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), 49, 69, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new UnderlineSpan(), 49, 69, 69);

        wordtoSpan.setSpan(new UnderlineSpan(), 74, 88, 86);

        wordtoSpan.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), TermsAndConditionActivity.class);
                i.putExtra(Constants.INTENT_TYPE_CONDTION, true);
                startActivity(i);
            }
        }, 49, 69, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        wordtoSpan.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), TermsAndConditionActivity.class);
                i.putExtra(Constants.INTENT_TYPE_CONDTION, false);
                startActivity(i);
            }
        }, 74, 88, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvDetails.setText(wordtoSpan);
        tvDetails.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void createButtonStarted() {
        btn_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });
    }
}
