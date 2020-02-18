package com.apps.mysweet.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.mysweet.model.Product;
import com.apps.mysweet.R;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ProductsHomeAdapter extends FirestoreRecyclerAdapter<Product, ProductsHomeAdapter.ViewHolder> {


    public ProductsHomeAdapter(@NonNull FirestoreRecyclerOptions<Product> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Product model) {
        holder.bind(model);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_product, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder {

          ImageView productImageView;
          TextView productNameTextView;
          TextView productSugarTextView;
          TextView productPriceTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.image_product_home);
            productNameTextView = itemView.findViewById(R.id.name_product_home);
            productSugarTextView = itemView.findViewById(R.id.sugar_product_home);
            productPriceTextView = itemView.findViewById(R.id.price_product_home);

        }

        void bind(Product product) {


            productNameTextView.setText(product.getProductName());
            productSugarTextView.setText("Sugar "+product.getPercentageOfSugar()+"%");
            productPriceTextView.setText(product.getPrice()+"₪  ");
            Glide.with(productImageView).load(product.getProductPhotoUrl()).into(productImageView);

        }
    }
}
