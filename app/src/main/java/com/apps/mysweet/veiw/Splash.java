package com.apps.mysweet.veiw;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.apps.mysweet.R;
import com.google.firebase.auth.FirebaseAuth;

public class Splash extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 1200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        hideStaustBar();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(Splash.this, MainActivity.class);
                Intent homeIntent = new Intent(Splash.this, HomeActivity.class);
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    Splash.this.startActivity(homeIntent);
                } else {
                    Splash.this.startActivity(mainIntent);
                }
                Splash.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }


    private void hideStaustBar() {
        View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.
        if (getActionBar() != null) {
            ;

            getActionBar().hide();
        }
    }
}

