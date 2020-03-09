package com.apps.mysweet.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apps.mysweet.model.Order;
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

import java.util.ArrayList;

public class OrderViewModel extends AndroidViewModel {
    private MutableLiveData<FirestoreRecyclerOptions<Product>> optionsMyProductsLiveData;
    private MutableLiveData<ArrayList<String>> listMutableLiveData;
    private MutableLiveData<Order> orderLiveData;
    private MutableLiveData<Task<QuerySnapshot>> taskLiveData;
    ArrayList<String> list;
    ArrayList<String> list2;


    public OrderViewModel(@NonNull Application application) {
        super(application);
        optionsMyProductsLiveData = new MutableLiveData<>();
       list = new ArrayList<>();
        list2 = new ArrayList<>();
        listMutableLiveData = new MutableLiveData<>();
        orderLiveData = new MutableLiveData<>();
        taskLiveData = new MutableLiveData<>();

    }

    public MutableLiveData<Task<QuerySnapshot>> getTaskMyProducts() {

          FirebaseFirestore.getInstance().collection("orders")
                .document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                .collection("myProducts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                           taskLiveData.setValue(task);
                    }
                });
        return taskLiveData;
    }

    public MutableLiveData<FirestoreRecyclerOptions<Product>> getListMyProducts() {

        Query query = FirebaseFirestore.getInstance().collection("orders")
                .document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                .collection("myProducts");
        optionsMyProductsLiveData.setValue(new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(query, Product.class)
                .build());
        return optionsMyProductsLiveData;
    }

    public void getSubTotalData(final BasketFragment.FirestoreCallBack firestoreCallBack) {
        FirebaseFirestore.getInstance().collection("orders")
                .document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                .collection("myProducts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int subTotalPrice = 0;
                for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
                    subTotalPrice += snapshot.toObject(Product.class).getPrice() *
                            snapshot.toObject(Product.class).getQuantity();

                    firestoreCallBack.onCallBack(subTotalPrice);
                }
            }
        });
    }

    public void updateProduct(Product product) {

        FirebaseFirestore.getInstance().collection("orders")
                .document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                .collection("myProducts").
                document(product.getProductId()).set(product).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });
    }

    public void AddTitleToList(final String title) {
        FirebaseFirestore.getInstance().collection("orders")
                .document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Order order1 = task.getResult().toObject(Order.class);
                list = order1.getListTitles();

                list.add(title);
                order1.setListTitles(list);
                order1.setAddress(title);

                FirebaseFirestore.getInstance().collection("orders")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                        .set(order1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("ttt", "success in  add Title0,.t4.");

                    }
                });

            }
        });
    }

    public MutableLiveData<ArrayList<String>> getListTitles() {
        FirebaseFirestore.getInstance().collection("orders")
                .document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                listMutableLiveData.setValue(task.getResult().toObject(Order.class).getListTitles());
            }
        });
        return listMutableLiveData;
    }
    public  MutableLiveData<Order>getOrder(){
        FirebaseFirestore.getInstance().collection("orders").document(FirebaseAuth.getInstance()
                .getCurrentUser().getPhoneNumber()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                  orderLiveData.setValue(task.getResult().toObject(Order.class));
            }
        });
     return  orderLiveData;
    }

    public void updateAddress(String address) {
        FirebaseFirestore.getInstance().collection("orders")
                .document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).update("address", address).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("ttt", "success  ");

            }
        });

    }


}