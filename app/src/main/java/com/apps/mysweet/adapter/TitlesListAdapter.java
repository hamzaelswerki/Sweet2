package com.apps.mysweet.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.mysweet.R;

import java.util.ArrayList;

public class TitlesListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> list;
     //Todo edite to position user
    private int selectedPosition = 0;
    OnRadioClickListener onRadioClickListener;

    public TitlesListAdapter(Context context, ArrayList<String> list) {
        this.list = list;
        this.context = context;
    }

   public interface OnRadioClickListener{
        void onRadioClicked(String address);
    }

    public void setOnRadioClickListener(OnRadioClickListener onRadioClickListener){
        this.onRadioClickListener=onRadioClickListener;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_adress, parent, false);
            vh = new ViewHolder();
            vh.tvTitle = convertView.findViewById(R.id.tv_title);
            vh.textViewWordTitle = convertView.findViewById(R.id.textView20);
            vh.radioButton = convertView.findViewById(R.id.radio_btn);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tvTitle.setText(list.get(position));

        vh.radioButton.setChecked(position == selectedPosition);

        if (onRadioClickListener!=null&&position == selectedPosition) {
            onRadioClickListener.onRadioClicked(vh.tvTitle.getText().toString());
        }
            if (position==selectedPosition) {
                vh.tvTitle.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                vh.textViewWordTitle.setTextColor(context.getResources().getColor(R.color.titles_profile));
            }else {
                vh.tvTitle.setTextColor(context.getResources().getColor(R.color.grey_color));
                vh.textViewWordTitle.setTextColor(context.getResources().getColor(R.color.grey_color));

            }
        vh.radioButton.setTag(position);
        vh.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedPosition = (Integer) view.getTag();
                notifyDataSetChanged();
                if (onRadioClickListener!=null)
               onRadioClickListener.onRadioClicked(vh.tvTitle.getText().toString());
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tvTitle;
        TextView textViewWordTitle;
        RadioButton radioButton;

    }
}
