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
import android.widget.Toast;

import com.apps.mysweet.model.Constants;
import com.apps.mysweet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginNumberFragment extends Fragment {

    EditText editTextLoginNumber;
    ImageView imageNext;
    FirebaseAuth mAuth;
    String codeSent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login_number, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTextLoginNumber = getView().findViewById(R.id.edit_text_login_number);
        imageNext = getView().findViewById(R.id.image_next);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        createEditeTextLogin();
        createImageNext();

        getView().findViewById(R.id.buttonhhhh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
      //  verifySignInCode();
            }
        });

    }
    private  void verifySignInCode(){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, "123456");;
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(),"signInWithCredential",Toast.LENGTH_SHORT).show();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("ttt", "signInWithCredential:success");

                         //   FirebaseUser user = task.getResult().getUser();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.d("ttt", "signInWithCredential:failure");

                        }
                    }
                });
    }


    private void createEditeTextLogin() {
        editTextLoginNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 9) {
                    imageNext.setVisibility(View.VISIBLE);
                } else {
                    imageNext.setVisibility(View.GONE);

                }
            }
        });

    }

    private void createImageNext() {
        imageNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               sendVerificationCode();
            }
        });
    }

    private void sendVerificationCode() {
         String phoneNumber =editTextLoginNumber.getText().toString();
           if (!phoneNumber.startsWith("5")){
                   editTextLoginNumber.setError("Invalid Number");
                    editTextLoginNumber.requestFocus();
                    return;
           }
             phoneNumber=Constants.CODE_PALESTINE+phoneNumber;
               PhoneAuthProvider.getInstance().verifyPhoneNumber(
                       phoneNumber,        // Phone number to verify
                       60,                 // Timeout duration
                       TimeUnit.SECONDS,   // Unit of timeout
                       getActivity(),               // Activity (for callback binding)
                       mCallbacks);        // OnVerificationStateChangedCallbacks
        Log.d("ttt",phoneNumber+">>*----");

    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            Log.d("ttt","complete");

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.d("ttt","failed"+e.getMessage());
            Toast.makeText(getContext(),"Enter vaild Number",Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
           super.onCodeSent(s,forceResendingToken);
            codeSent=s;

            Log.d("ttt","code sent");
            Bundle bundle=new Bundle();
            bundle.putString(Constants.SEND_CODE,codeSent);
            Login2Fragment login2Fragment= new Login2Fragment();
            login2Fragment.setArguments(bundle);
     getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_login, login2Fragment).addToBackStack("back").commit();

        }

        @Override
        public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
            super.onCodeAutoRetrievalTimeOut(s);
            Log.d("ttt","onCodeAutoRetrievalTimeOut");

        }
    };

}

