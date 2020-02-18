package com.apps.mysweet.veiw;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.apps.mysweet.R;
import com.apps.mysweet.model.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login2Fragment extends Fragment {
    FirebaseAuth mAuth;
    String codeSent;
    ImageView imageNext;
    EditText editTextLoginNumberVerifyCode;

    public Login2Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageNext = getView().findViewById(R.id.image_next_verify_code);
        editTextLoginNumberVerifyCode = getView().findViewById(R.id.edti_text_veify_code);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        createImageNext();
        createEditeTextLogin();


    }

    private void createImageNext() {
        imageNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeSent = getArguments().getString(Constants.SEND_CODE);
                verifySignInCode(codeSent);

            }
        });
    }

    private void createEditeTextLogin() {
        editTextLoginNumberVerifyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 6) {
                    imageNext.setVisibility(View.VISIBLE);
                } else {
                    imageNext.setVisibility(View.GONE);

                }
            }
        });

    }

    private void verifySignInCode(String codeSent) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, editTextLoginNumberVerifyCode.getText().toString());
        ;
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            getActivity().getSupportFragmentManager().beginTransaction().
                                    replace(R.id.frame_login, new LoginWithEmailFragment()).commit();

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.d("ttt", "signInWithCredential:failure");

                        }
                    }
                });

    }

}