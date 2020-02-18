package com.apps.mysweet.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.mysweet.model.AppVideo;
import com.apps.mysweet.R;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public   class VideoAdapter extends FirestoreRecyclerAdapter<AppVideo, VideoAdapter.ViewHolder> {


    public VideoAdapter(@NonNull FirestoreRecyclerOptions<AppVideo> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull AppVideo model) {
         holder.bind(model);
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item_for_video, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewForVideo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewForVideo = itemView.findViewById(R.id.photo_image_video);

        }

        void bind(AppVideo appVideo) {
            Glide.with(imageViewForVideo).load(appVideo.getPhotoUrl()).into(imageViewForVideo);

        }
    }
}
