package com.apps.mysweet.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apps.mysweet.model.Product;
import com.apps.mysweet.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserInfoViewModel  extends AndroidViewModel {
    private MutableLiveData<com.apps.mysweet.model.User> user;


    public UserInfoViewModel(@NonNull Application application) {
        super(application);
        user=new MutableLiveData<>();

    }
    public MutableLiveData<User> getUserInfo(){
        FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
              user.setValue(task.getResult().toObject(User.class));
            }
        });
          return user;
    }
    public void updateUser( User user){

        FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                .set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                 Log.d("ttt","success updated user");
            }
        });
    }

}