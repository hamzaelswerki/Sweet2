package com.apps.mysweet.viewmodel;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apps.mysweet.model.Branch;
import com.apps.mysweet.model.Chat;
import com.apps.mysweet.veiw.ChatBranch;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ChatViewModel extends AndroidViewModel {
    private MutableLiveData<FirestoreRecyclerOptions<Chat>> optionsMyMEsagesLiveData;
    private MutableLiveData<Branch> branchMutableLiveData;
    Branch branch;

    public ChatViewModel(@NonNull Application application) {
        super(application);
        optionsMyMEsagesLiveData = new MutableLiveData<>();
      branchMutableLiveData =new MutableLiveData<>();
      branch=new Branch();
    }

    public MutableLiveData<FirestoreRecyclerOptions<Chat>> getListMessages(String idForBranchDocument) {

        Query query = FirebaseFirestore.getInstance().collection("branchs").
                document(idForBranchDocument)
                .collection(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).orderBy("time");
        optionsMyMEsagesLiveData.setValue(new FirestoreRecyclerOptions.Builder<Chat>()
                .setQuery(query, Chat.class)
                .build());
        return optionsMyMEsagesLiveData;
    }

    public void addMessageToChat(Chat chat) {

        FirebaseFirestore.getInstance().collection("branchs").
                document(chat.getIdBranch())
                .collection(chat.getIdUser()).add(chat).addOnSuccessListener
                (new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("ttt", "sucess added message");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("ttt", "Error" + e.getMessage());
                    }});

    }


public   MutableLiveData<Branch> getIdForBranch(final String nameBranch){
    FirebaseFirestore.getInstance().collection("branchs").whereEqualTo("name", nameBranch)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            Log.d("ttt", document.getId() + " =>  doumentID " + document.getData());
                         branch =document.toObject(Branch.class);
              branch.setIdBranch(document.getId());
              branchMutableLiveData.setValue( branch);

                          //  startActivity(new Intent(getApplicationContext(), ChatBranch.class).putExtra("branchName",nameBranch).putExtra("branchId",idBranch));
                        }
                    } else {
                        Log.d("TAG", "Error getting documents.");
                    }
                }
            });
    return  branchMutableLiveData;

}
}
