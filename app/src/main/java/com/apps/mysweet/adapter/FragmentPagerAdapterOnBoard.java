package com.apps.mysweet.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.List;

public class FragmentPagerAdapterOnBoard extends androidx.fragment.app.FragmentPagerAdapter {
   static     List<Fragment>fragmentList;
  int myPosition;

  public FragmentPagerAdapterOnBoard(@NonNull FragmentManager fm, List<Fragment>fragmentList) {
        super(fm);
        this.fragmentList=fragmentList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
     //   myPosition=position;

        return fragmentList.get(position);

    }


    @Override
    public int getCount() {
        return fragmentList.size();
    }

}
