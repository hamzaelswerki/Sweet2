package com.apps.mysweet.adapter;

import android.util.Log;
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

public class ProductsListAdapter extends FirestoreRecyclerAdapter<Product, ProductsListAdapter.ViewHolder> {
    OnItemViewClickListener onItemClickListener;

    public  interface  OnItemViewClickListener{
        void OnClicked(Product product);
    }

   public void setOnItemViewClickListener(OnItemViewClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ProductsListAdapter(@NonNull FirestoreRecyclerOptions<Product> options) {
        super(options);
        Log.d("ttt","cons");

    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Product model) {
           holder.bind(model);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_products,parent,false));
    }

    class  ViewHolder extends RecyclerView.ViewHolder {
    ImageView imageViewProduct;
    TextView tvPrice;
        TextView tvNamrProduct;

        public ViewHolder(@NonNull View itemView) {
         super(itemView);
     imageViewProduct=itemView.findViewById(R.id.imageView_products_list);
     tvPrice=itemView.findViewById(R.id.price_products_list);
    tvNamrProduct=itemView.findViewById(R.id.name_products_list);

    itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (onItemClickListener!=null){
                onItemClickListener.OnClicked(getItem(getAdapterPosition()));
            }
        }
    });
     }
 void  bind(Product product){
     Log.d("ttt", product.getProductId()+" = id list ");

     tvNamrProduct.setText(product.getProductName());
     tvPrice.setText("₪ "+product.getPrice()+".00");
     Glide.with(imageViewProduct).load(product.getProductPhotoUrl()).into(imageViewProduct);

     }
 }
}
