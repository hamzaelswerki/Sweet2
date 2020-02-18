package com.apps.mysweet.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.apps.mysweet.model.AppVideo;
import com.apps.mysweet.model.Category;
import com.apps.mysweet.model.Product;
import com.apps.mysweet.veiw.BasketFragment;
import com.apps.mysweet.veiw.DetailsProductActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class HomeViewModel extends AndroidViewModel {
    private MutableLiveData<FirestoreRecyclerOptions<Category>> optionsCategoryLiveData;
    private MutableLiveData<FirestoreRecyclerOptions<Product>> optionsBestProductLiveData;
    private MutableLiveData<FirestoreRecyclerOptions<Product>> optionsRecommendedProductLiveData;
    private MutableLiveData<FirestoreRecyclerOptions<AppVideo>> optionsVideoLiveData;
    private MutableLiveData<FirestoreRecyclerOptions<Product>> optionsListProductsLiveData;
    Product product=null;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        optionsCategoryLiveData = new MutableLiveData<>();
        optionsBestProductLiveData = new MutableLiveData<>();
        optionsRecommendedProductLiveData = new MutableLiveData<>();
        optionsVideoLiveData = new MutableLiveData<>();
        optionsListProductsLiveData=new MutableLiveData<>();

    }

    public LiveData<FirestoreRecyclerOptions<Category>> getCategoriesData() {

        Query query = FirebaseFirestore.getInstance().collection("categories").orderBy("categoryName").limit(10);
        optionsCategoryLiveData.setValue(new FirestoreRecyclerOptions.Builder<Category>()
                .setQuery(query, Category.class)
                .build());
        return optionsCategoryLiveData;
    }

    public LiveData<FirestoreRecyclerOptions<Product>> getDataBestProducts() {

        Query query = FirebaseFirestore.getInstance().collection("products").whereEqualTo("isBestProduct", true).limit(10);
        optionsBestProductLiveData.setValue(new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(query, Product.class)
                .build());
        return optionsBestProductLiveData;
    }

    public LiveData<FirestoreRecyclerOptions<Product>> getDataRecommendedProducts() {

        Query query = FirebaseFirestore.getInstance().collection("products").whereEqualTo("isRecommended", true).limit(10);
        optionsRecommendedProductLiveData.setValue(new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(query, Product.class)
                .build());
        return optionsRecommendedProductLiveData;
    }

    public MutableLiveData<FirestoreRecyclerOptions<AppVideo>> getDataVideo() {

        Query query = FirebaseFirestore.getInstance().collection("videos").limit(10);
        optionsVideoLiveData.setValue(new FirestoreRecyclerOptions.Builder<AppVideo>()
                .setQuery(query, AppVideo.class)
                .build());
        return optionsVideoLiveData;
    }
    public MutableLiveData<FirestoreRecyclerOptions<Product>> getListProducts(String category) {

        Query query = FirebaseFirestore.getInstance().collection("products").whereEqualTo("category", category).limit(10);
        optionsListProductsLiveData.setValue(new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(query, Product.class)
                .build());
        return optionsListProductsLiveData;
    }

    ////TODO need to change to MutableLiveData
    public Product getProductByNameProduct(final String productName,  final DetailsProductActivity.DetailsFirestoreCallBack detailsFirestoreCallBack){

      FirebaseFirestore.getInstance().collection("products").whereEqualTo("productName", productName)
              .get()
              .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                  @Override
                  public void onComplete(@NonNull Task<QuerySnapshot> task) {
                      if (task.isSuccessful()) {
                          for (QueryDocumentSnapshot document : task.getResult()) {

                              Log.d("TAG", document.getId() + " => " + document.getData());
                                product = document.toObject(Product.class);
                                product.setProductId(document.getId());

                              detailsFirestoreCallBack.onCallBackGetProduct(product,productName);

                          }
                      } else {
                          Log.d("TAG", "Error getting documents.");
                      }
                  }
              });
      return product;
  }


}
