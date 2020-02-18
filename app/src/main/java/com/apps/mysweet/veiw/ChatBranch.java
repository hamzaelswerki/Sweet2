package com.apps.mysweet.veiw;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.mysweet.R;
import com.apps.mysweet.adapter.ChatAdapter;
import com.apps.mysweet.model.Chat;
import com.apps.mysweet.viewmodel.ChatViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class ChatBranch extends AppCompatActivity {
    TextView nameBranch;

    RecyclerView recyclerView;
    ArrayList<Chat> msgs = new ArrayList<>();
    EditText ed_messege;
    ImageView img_send;
    ImageButton btnBake;
    ChatAdapter chatAdabter;
    FirebaseFirestore firebaseFirestore;
    ChatViewModel chatViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_branch);
        nameBranch = findViewById(R.id.text_title_condition);
        recyclerView = findViewById(R.id.list_chat);
        ed_messege = findViewById(R.id.edit_chat);
        img_send = findViewById(R.id.img_send);
        btnBake = findViewById(R.id.btn_back);
        firebaseFirestore = FirebaseFirestore.getInstance();

        nameBranch.setText(getIntent().getStringExtra("branchName"));
        setStatusBarColor("#FF3459");


        chatViewModel = ViewModelProviders.of(this).get(ChatViewModel.class);
        getMessages();


  /*      FirebaseFirestore.getInstance().collection("branchs").
                document(getIntent().getStringExtra("branchId"))
                .collection(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
                    Chat chat = snapshot.toObject(Chat.class);

                    msgs.add(chat);

                    chatAdabter.notifyDataSetChanged();
                }
            }
        });

*/
        img_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMsg();
            }


        });

        btnBake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (chatAdabter != null) {
            chatAdabter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (chatAdabter != null) {
            chatAdabter.stopListening();
        }
    }

    private void sendMsg() {
        String message = ed_messege.getText().toString().trim();
        if (!message.equals("") && message != null) {
            ed_messege.setText("");

            Chat chat = new Chat(message, FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber(),
                    getIntent().getStringExtra("branchId"), Timestamp.now(), "user");


            chatViewModel.addMessageToChat(chat);

       //     msgs.add(chat);

            chatAdabter.notifyDataSetChanged();

        }

    }

    private void setStatusBarColor(String colorString) {

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor(colorString));
    }


    private void getMessages() {

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(llm);

        chatViewModel.getListMessages(getIntent().getStringExtra("branchId")).observe(this, new Observer<FirestoreRecyclerOptions<Chat>>() {
            @Override
            public void onChanged(FirestoreRecyclerOptions<Chat> chatFirestoreRecyclerOptions) {
                Log.d("ttt",   getIntent().getStringExtra("branchId")+" => branchID  in Chat  = " );

                chatAdabter = new ChatAdapter(chatFirestoreRecyclerOptions);
                chatAdabter.startListening();
                recyclerView.setAdapter(chatAdabter);
            }


        });

    }
}