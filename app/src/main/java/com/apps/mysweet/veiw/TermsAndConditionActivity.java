package com.apps.mysweet.veiw;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.apps.mysweet.R;
import com.apps.mysweet.model.Constants;

public class TermsAndConditionActivity extends AppCompatActivity {
    Toolbar toolbar;
    Boolean isTypeCondition;
    TextView textViewTitle;
    ImageButton buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_condition);
///add Fragment TermsAndConditionFragment on Activity
        getSupportFragmentManager().beginTransaction().add(R.id.frame_conditions, new TermsAndConditionFragment()).commit();


        createToolbar();
        createButtonBack();

    }

    private void createToolbar() {
        toolbar = findViewById(R.id.toolbar_condition);
        textViewTitle = findViewById(R.id.text_title_condition);
        setSupportActionBar(toolbar);
        isTypeCondition = getIntent().getBooleanExtra(Constants.INTENT_TYPE_CONDTION, false);
        if (isTypeCondition) {
            textViewTitle.setText(getResources().getString(R.string.condition_title));
        } else {
            textViewTitle.setText(getResources().getString(R.string.policy_title));

        }
    }
    private void createButtonBack(){
        buttonBack = findViewById(R.id.btn_back);
         buttonBack.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 onBackPressed();
             }
         });

    }
}
