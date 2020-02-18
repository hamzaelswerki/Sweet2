package com.apps.mysweet.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.mysweet.R;

public class AdapterPhoto  extends RecyclerView.Adapter<AdapterPhoto.PhotoViewHolder>{
    int[] imagesIds ;
       public  AdapterPhoto(int []imagesIds){
            this.imagesIds=imagesIds;
       }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PhotoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
      holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return imagesIds.length;
    }

    class PhotoViewHolder extends  RecyclerView.ViewHolder {
        ImageView photoSaleImageView;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            photoSaleImageView=itemView.findViewById(R.id.photo_image_sale);
        }
        void bind(int position){
             photoSaleImageView.setImageResource(imagesIds[position]);
        }
    }

}
