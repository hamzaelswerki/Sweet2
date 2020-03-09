package com.apps.mysweet.veiw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.mysweet.R;
import com.apps.mysweet.model.Constants;
import com.apps.mysweet.model.Product;
import com.apps.mysweet.viewmodel.HomeViewModel;
import com.apps.mysweet.viewmodel.OrderViewModel;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class DetailsProductActivity extends AppCompatActivity {
    ImageView productImg;
    TextView tvProductName;
    TextView priceProduct;

    TextView votesNumberTextView;
    TextView numOfPersonTv;
    TextView tvInstructions;
    TextView tvQuantity;
    TextView tvFinalQuntity;
    TextView tvQuntityPrice;
    Button plusButton;
    Button minesButton;
    Button viewCartButton;
    int count;
    int priceFinal;
    int price;

Product productForMyProductCollection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_product);


        hideStaustBar();
        bindingView();
        createPlusButtons();
       // receiveFromIntenr();
        getPro();
        createTvQuantity();
       createViewCartButton();
    }


    public interface DetailsFirestoreCallBack{
        void onCallBackGetProduct(Product product,String productName);

    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("ttt", "start");
        hideStaustBar();

    }


    private void createTvQuantity() {

        tvQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tvFinalQuntity.setText(editable + getResources().getQuantityString(R.plurals.ITEMS, Integer.parseInt(editable.toString())));
                priceFinal = Integer.parseInt(editable.toString()) * price;
                tvQuntityPrice.setText("₪" + priceFinal + ".00 Incl taxes");

            }
        });
    }

    private void createPlusButtons() {

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = Integer.parseInt(tvQuantity.getText().toString());
                count += 1;
                tvQuantity.setText(String.valueOf(count));

            }
        });
        minesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count > 1) {
                    count -= 1;
                    tvQuantity.setText(String.valueOf(count));
                }
            }
        });

    }


    private void bindingView() {
        productImg = findViewById(R.id.imageView12);
        tvProductName = findViewById(R.id.name_product_details);
        priceProduct = findViewById(R.id.price_product_details);
        votesNumberTextView = findViewById(R.id.text_view_votes);
        numOfPersonTv = findViewById(R.id.text_num_person_details);
        tvInstructions = findViewById(R.id.textView_instracion_details);
        tvQuantity = findViewById(R.id.textView_quantity_details);
        tvFinalQuntity = findViewById(R.id.textView19_quantity_items_details);
        tvQuntityPrice = findViewById(R.id.price_quantity_details_textView20);
        plusButton = findViewById(R.id.button2_puls);
        minesButton = findViewById(R.id.button_mines);
        viewCartButton = findViewById(R.id.view_cart_button3);
    }

      private  void  getPro(){
          HomeViewModel homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
          String productName = getIntent().getStringExtra(Constants.INTENT_PRODUCT_NAME);

          homeViewModel.getProductByNameProduct(productName, new DetailsFirestoreCallBack() {
              @Override
              public void onCallBackGetProduct(Product product, String productName) {
                  productForMyProductCollection=product;
                  Log.d("ttt",product.getProductId()+"  details id ");

                  fillDataInViews(product);
              }
          });
      }


    private void createViewCartButton() {
        viewCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 productForMyProductCollection.setQuantity(Integer.parseInt(tvQuantity.getText().toString()));
                Log.d("ttt",productForMyProductCollection.getProductId()+"  my prod id ");

                FirebaseFirestore firestore=FirebaseFirestore.getInstance();
                firestore.collection("orders").
                        document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                        .collection("myProducts").document(productForMyProductCollection.getProductId())
                        .set(productForMyProductCollection).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("ttt", "Error"+e.getMessage());

                    }
                });

            }
        });
    }

    private void fillDataInViews(Product product) {
        Glide.with(productImg).load(product.getProductPhotoUrl()).into(productImg);
        tvProductName.setText(product.getProductName());
        priceProduct.setText("₪ " + product.getPrice() + ".00");
        votesNumberTextView.setText(product.getVotes() + " " + getString(R.string.votes));
        numOfPersonTv.setText(getString(R.string.number_persons) + " " + product.getNumberOfPersons());
        tvInstructions.setText(product.getInstrucations());

        tvFinalQuntity.setText(tvQuantity.getText() + getResources().getQuantityString(R.plurals.ITEMS, Integer.parseInt(tvQuantity.getText().toString())));
        price = product.getPrice();
        int priceFinal = Integer.parseInt(tvQuantity.getText().toString()) * product.getPrice();
        tvQuntityPrice.setText("₪" + priceFinal + ".00 Incl taxes");
    }

    private void hideStaustBar() {
        View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.
        if (getActionBar() != null) {
            ;

            getActionBar().hide();
        }
    }

}
