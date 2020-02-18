package com.apps.mysweet.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apps.mysweet.model.Chat;
import com.apps.mysweet.model.Product;
import com.apps.mysweet.veiw.BasketFragment;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class OrderViewModel extends AndroidViewModel {
    private MutableLiveData<FirestoreRecyclerOptions<Product>> optionsMyProductsLiveData;

    public OrderViewModel(@NonNull Application application) {
        super(application);
        optionsMyProductsLiveData = new MutableLiveData<>();

    }


    public MutableLiveData<FirestoreRecyclerOptions<Product>> getListMyProducts() {

        Query query = FirebaseFirestore.getInstance().collection("orders")
                .document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).collection("myProducts");

        optionsMyProductsLiveData.setValue(new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(query, Product.class)
                .build());
        return optionsMyProductsLiveData;
    }
    ////TODO need to change to MutableLiveData
    public void getSubTotalData(final BasketFragment.FirestoreCallBack firestoreCallBack) {
        FirebaseFirestore.getInstance().collection("orders")
                .document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                .collection("myProducts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int subTotalPrice=0;
                for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
                    subTotalPrice += snapshot.toObject(Product.class).getPrice() *
                            snapshot.toObject(Product.class).getQuantity();

                    firestoreCallBack.onCallBack(subTotalPrice);
                }
            }
        });
    }

    public void updateProduct(   Product product){

        FirebaseFirestore.getInstance().collection("orders")
                .document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                .collection("myProducts").
                document(product.getProductId()).set(product).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("ttt","success in VMMV");
            }
        });
    }





}