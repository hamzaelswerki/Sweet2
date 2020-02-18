package com.apps.mysweet.veiw;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.apps.mysweet.R;
import com.apps.mysweet.adapter.FragmentPagerAdapterOnBoard;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
  public static ViewPager viewPager;
    WormDotsIndicator springDotsIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    createViewPager();
    hideStaustBar();

    }
   void createViewPager(){

      viewPager = findViewById(R.id.view_pager);
      ArrayList<Fragment> fragmentList = new ArrayList<>(Arrays.asList(new OnBoard1Fragment(), new OnBoard2Fragment(), new OnBoard3Fragment()));
      FragmentPagerAdapterOnBoard adapter = new FragmentPagerAdapterOnBoard(getSupportFragmentManager(), fragmentList);
       springDotsIndicator = (WormDotsIndicator) findViewById(R.id.spring_dots_indicator);
       viewPager.setAdapter(adapter);

       springDotsIndicator.setViewPager(viewPager);

  }
    private void hideStaustBar(){
        View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.
        if(getActionBar()!=null){;

            getActionBar().hide();
        }
    }

}
