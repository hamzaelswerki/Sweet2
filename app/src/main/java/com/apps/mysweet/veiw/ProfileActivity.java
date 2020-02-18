package com.apps.mysweet.veiw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.apps.mysweet.R;
import com.apps.mysweet.model.Constants;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    Button settingButton;
    Button aboutButton;
    Button singOutButton;
    ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profileImageView = findViewById(R.id.image_view_profile_person);

        settingButton = findViewById(R.id.setting_button);
        aboutButton = findViewById(R.id.about_button);
        singOutButton = findViewById(R.id.sign_button);

        settingButton.setOnClickListener(this);
        aboutButton.setOnClickListener(this);
        singOutButton.setOnClickListener(this);
        profileImageView.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_button:
                startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                break;
            case R.id.about_button:

                startActivity(new Intent(getApplicationContext(), TermsAndConditionActivity.class).putExtra(Constants.INTENT_TYPE_CONDTION,true));
                break;
            case R.id.sign_button:
                //TODO make signOutButton
                break;
            case R.id.image_view_profile_person:
                startActivity(new Intent(getApplicationContext(),EditeProfileActivity.class));

        }
    }
}
