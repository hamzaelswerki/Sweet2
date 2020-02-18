package com.apps.mysweet.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.mysweet.R;
import com.apps.mysweet.model.Product;
import com.apps.mysweet.veiw.BasketFragment;
import com.apps.mysweet.viewmodel.OrderViewModel;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyProductsAdapter extends FirestoreRecyclerAdapter<Product, MyProductsAdapter.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     */

    public  interface  OnUpdateQuantity{
        void onUpdated(Product product);
    }
    OnUpdateQuantity onUpdateQuantity;
   public void setOnbuttonUpdatesLustener(OnUpdateQuantity onUpdateQuantity){
        this.onUpdateQuantity=onUpdateQuantity;
    }

    public MyProductsAdapter(@NonNull FirestoreRecyclerOptions<Product> options) {
        super(options);

    }


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Product model) {
        model.setProductId(this.getSnapshots().getSnapshot(position).getId());
        holder.bind(model);
   holder.createTvQuantity(model);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_order, parent, false));


    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productImageView;
        TextView productNameTextView;
        TextView numOfPersonsTextView;
        TextView quantityTextView;
        TextView priceTextView;
        Button plusButton;

        Button minusButton;


        int count;
        int priceFinal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImageView = itemView.findViewById(R.id.order_imageView11);
            productNameTextView = itemView.findViewById(R.id.order_name_pro);
            priceTextView = itemView.findViewById(R.id.price_order_pro);
            numOfPersonsTextView = itemView.findViewById(R.id.order_tv_num_person);
            quantityTextView = itemView.findViewById(R.id.order_tv_quantity);
            plusButton = itemView.findViewById(R.id.order_button2_puls);
            minusButton = itemView.findViewById(R.id.order_button_mines);

        }

        private void bind(Product product) {

            Glide.with(productImageView).load(product.getProductPhotoUrl()).into(productImageView);

            productNameTextView.setText(product.getProductName());
            numOfPersonsTextView.setText(itemView.getContext().getString(R.string.number_persons) + " " + product.getNumberOfPersons());

            quantityTextView.setText(product.getQuantity() + "");

            priceFinal = Integer.parseInt(quantityTextView.getText().toString()) * product.getPrice();

            priceTextView.setText("₪ " + priceFinal + ".00");

            createPlusButtons();
        }



        private void createTvQuantity(final Product product) {
            quantityTextView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    priceFinal = Integer.parseInt(editable.toString()) * product.getPrice();

                    priceTextView.setText("₪ " + priceFinal + ".00");
                         product.setQuantity(Integer.parseInt(editable.toString()));
                         onUpdateQuantity.onUpdated(product);

                    /*

                    FirebaseFirestore.getInstance().collection("orders")
                            .document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                            .collection("myProducts").document(product.getProductId())
                            .update("quantity", Integer.parseInt(editable.toString()))
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                        Log.d("ttt", "success updates");
                                        Log.d("ttt", product.getProductId()+"is updated");
                               //     onUpdateQuantity.onUpdated(true);

                                }
                            });
*/
                }
            });
        }
        private void createPlusButtons() {
            count = Integer.parseInt(quantityTextView.getText().toString());

            plusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    count = Integer.parseInt(quantityTextView.getText().toString());
                    count += 1;
                    quantityTextView.setText(String.valueOf(count));

                }
            });
            minusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (count > 1) {
                        count -= 1;
                        quantityTextView.setText(String.valueOf(count));
                    }
                }
            });

        }

    }
}

