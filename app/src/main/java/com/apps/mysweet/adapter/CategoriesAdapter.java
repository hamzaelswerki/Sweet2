package com.apps.mysweet.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.apps.mysweet.R;

import com.apps.mysweet.model.Category;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class CategoriesAdapter extends FirestoreRecyclerAdapter<Category, CategoriesAdapter.ViewHolder> {

    private OnItemClickListener onItemClickListener;

   public interface OnItemClickListener {
      void onItemClicked(Category category);
    }

   public  void setOnItemClickListener (OnItemClickListener onItemClickListener){
       this.onItemClickListener=onItemClickListener;
   }
    public CategoriesAdapter(@NonNull FirestoreRecyclerOptions<Category> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Category model) {
        holder.bind(model);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder {

          ImageView imageViewCategory;
        private TextView categeoryNameTextView;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            imageViewCategory = itemView.findViewById(R.id.image_category_item);
            categeoryNameTextView = itemView.findViewById(R.id.text_view_category_item);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener!=null){
                    Category category=getItem(getAdapterPosition());
                     onItemClickListener.onItemClicked(category);
                }
            }
        });
        }

        void bind(Category category) {
            categeoryNameTextView.setText(category.getCategoryName());
            Glide.with(imageViewCategory).load(category.getPhotoUrl()).into(imageViewCategory);
        }
    }
}
