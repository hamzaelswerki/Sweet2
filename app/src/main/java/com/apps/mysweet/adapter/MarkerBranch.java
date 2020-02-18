package com.apps.mysweet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apps.mysweet.R;
import com.apps.mysweet.model.Branch;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class MarkerBranch implements GoogleMap.InfoWindowAdapter {
    Context context;
    public MarkerBranch(Context context){
        this.context=context;


    }
    @Override
    public View getInfoWindow(Marker marker) {

        final View view= LayoutInflater.from(context).inflate(R.layout.branch_marker,null);
        view.setLayoutParams(new LinearLayout.LayoutParams(140,40));
        Branch branch= (Branch) marker.getTag();
        TextView textView_name=view.findViewById(R.id.name_branch);
        textView_name.setText(branch.getName());
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        final View view= LayoutInflater.from(context).inflate(R.layout.branch_marker,null);
        view.setLayoutParams(new LinearLayout.LayoutParams(140,40));
        Branch branch= (Branch) marker.getTag();
        TextView textView_name=view.findViewById(R.id.name_branch);
        textView_name.setText(branch.getName());
        return view;
    }
}
