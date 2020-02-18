package com.apps.mysweet.veiw;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.apps.mysweet.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
     getSupportFragmentManager().beginTransaction().add(R.id.frame_login,new LoginNumberFragment()).commit();
    }
}
