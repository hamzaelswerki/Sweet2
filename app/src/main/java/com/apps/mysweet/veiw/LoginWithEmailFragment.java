package com.apps.mysweet.veiw;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apps.mysweet.R;
import com.apps.mysweet.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginWithEmailFragment extends Fragment {
    EditText editTextUserName;
    EditText editTextUserEmail;
    Button buttonGo;

    public LoginWithEmailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_with_email, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonGo = getView().findViewById(R.id.btn_go);
        editTextUserName = getView().findViewById(R.id.edit_text_name);
        editTextUserEmail = getView().findViewById(R.id.edit_text_email);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        createButtonGo();
    }

    private void createButtonGo() {
        buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        if (checkValidUserName(editTextUserName.getText().toString())&& checkIsVaildEmail(editTextUserEmail.getText().toString())){
            User user=new User(editTextUserName.getText().toString(),editTextUserEmail.getText().toString(),FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber(),null);
            registerUserOnCollection(user);
        }

            }
        });
    }
    private  void registerUserOnCollection(User user){
        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        firestore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(),"Add user successfuly",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(),HomeActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ttt", "Error"+e.getMessage());

            }
        });
    }

    private boolean checkIsVaildEmail(String emailAddress) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (emailAddress.isEmpty()) {
           editTextUserEmail.setError("Invalid Email");
         return false;
        } else {
            if (emailAddress.trim().matches(emailPattern)) {
                    return true;
            } else {
                editTextUserEmail.setError("Invalid Email");

                return false;
            }
            //
        }

    }
    public  boolean checkValidUserName(String Name) {
        String regx = "^[a-zA-Z \\s]+$";
        if (Name.isEmpty()) {
            editTextUserName.setError("Invalid Name");
            return false;
        } else {
            if (Name.trim().matches(regx)) {
                return true;
            } else {
                editTextUserName.setError("Invalid Name");

                return false;
            }
        }

    }
}
