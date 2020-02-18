package com.apps.mysweet.veiw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.mysweet.R;
import com.apps.mysweet.adapter.ProductsListAdapter;
import com.apps.mysweet.model.Constants;
import com.apps.mysweet.model.Product;
import com.apps.mysweet.viewmodel.HomeViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ListProductsActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView tvTitle;
    ImageView imageBack;
    RecyclerView listProducts;
    private ProductsListAdapter productsListAdapter;
  HomeViewModel homeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_products);

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        createToolBar();
        createImageViewBack();
       createRecyclerViewProducts();
    }

    private void createRecyclerViewProducts() {
      listProducts=findViewById(R.id.list_products);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listProducts.setLayoutManager(llm);
      homeViewModel.getListProducts(getIntent().getStringExtra(Constants.INTENT_CATEGEORY_NAME)).observe(this, new Observer<FirestoreRecyclerOptions<Product>>() {
          @Override
          public void onChanged(FirestoreRecyclerOptions<Product> productFirestoreRecyclerOptions) {
                  productsListAdapter=new ProductsListAdapter(productFirestoreRecyclerOptions);

                 productsListAdapter.setOnItemViewClickListener(new ProductsListAdapter.OnItemViewClickListener() {
                     @Override
                     public void OnClicked(Product product) {
                         Toast.makeText(getApplicationContext(),product.getProductName(),Toast.LENGTH_SHORT).show();
                      Intent intent=new Intent(getApplicationContext(),DetailsProductActivity.class);
                       intent.putExtra(Constants.INTENT_PRODUCT_NAME,product.getProductName());
                       startActivity(intent);

                     }
                 });
                   productsListAdapter.startListening();
                  listProducts.setAdapter(productsListAdapter);
          }
      });

    }


    private void createToolBar() {
        toolbar = findViewById(R.id.toolbar_list_products);
        setSupportActionBar(toolbar);
        tvTitle = findViewById(R.id.text_title_list_products);
        tvTitle.setText(getIntent().getStringExtra(Constants.INTENT_CATEGEORY_NAME));
        toolbar.setBackgroundColor(Color.parseColor(getIntent().getStringExtra(Constants.INTENT_CATEGEORY_COLOR)));
        setStatusBarColor(getIntent().getStringExtra(Constants.INTENT_CATEGEORY_COLOR));
    }

    private void setStatusBarColor(String colorString) {

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor(colorString));
    }

    private void createImageViewBack() {
        imageBack = findViewById(R.id.btn_back_list_product);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    if (productsListAdapter!=null)
     productsListAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (productsListAdapter!=null)
    productsListAdapter.stopListening();
    }
}
