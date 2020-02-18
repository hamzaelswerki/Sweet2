package com.apps.mysweet.veiw;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.apps.mysweet.R;
import com.apps.mysweet.model.Product;
import com.apps.mysweet.model.User;
import com.apps.mysweet.viewmodel.UserInfoViewModel;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class EditeProfileActivity extends AppCompatActivity {
    UserInfoViewModel userInfoViewModel;
    ImageView userImage;
    EditText userNameEdtieText;
    EditText userEmailEdtieText;
    EditText userPhoneEdtieText;
    Button doneButton;
    String photoUrl;
    private static final int REQUEST_PICK_PHOTO = 100;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edite_profile);

        userNameEdtieText = findViewById(R.id.user_name_editText);
        userEmailEdtieText = findViewById(R.id.user_email_editText);
        userPhoneEdtieText = findViewById(R.id.user_phone_editText);
        userImage = findViewById(R.id.user_image_view);
        doneButton = findViewById(R.id.doneButton);

        getUserInfos();
        checkPhoneNumber();
        createUserImageView();
            updateUser();
    }
    private void getUserInfos() {

        userInfoViewModel = ViewModelProviders.of(this).get(UserInfoViewModel.class);
        userInfoViewModel.getUserInfo().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                userNameEdtieText.setText(user.getUserName());
                userEmailEdtieText.setText(user.getEmailAddress());
                userPhoneEdtieText.setText(user.getPhoneNumber());

                if (user.getPhotoUrl()!=null) {
                       Glide.with(userImage).load(user.getPhotoUrl()).into(userImage);

                   }
            }
        });
    }


    private void createUserImageView() {
         userImage.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                   selectPhoto();
             }
         });
    }

    public void selectPhoto() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_PICK_PHOTO);
    }



    private String getFileExtension(Uri imageUri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(getContentResolver().getType(imageUri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            if (imageUri != null) {
                userImage.setImageURI(imageUri);
               Log.d("ttt",imageUri.toString());
            } } }



   /* private void storeImage(){
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference().
                child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

        storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        photoUrl = uri.toString();
                    }
                }); }});
                      }
*/

    private void updateUser() {
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkValidUserName(userNameEdtieText.getText().toString()) &&
                        checkIsVaildEmail(userEmailEdtieText.getText().toString()) &&
                        userPhoneEdtieText.getText().toString().startsWith("+97")) {

                    final StorageReference storageReference = FirebaseStorage.getInstance().getReference().
                            child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

                    storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    photoUrl = uri.toString();
                                    User user = new User(userNameEdtieText.getText().toString(),
                                            userEmailEdtieText.getText().toString(),
                                            userPhoneEdtieText.getText().toString(), photoUrl);
                                                    userInfoViewModel.updateUser(user);
                                                    Toast.makeText(getApplicationContext(),"updated",Toast.LENGTH_SHORT).show();
                                }
                            }); }});


                } else {
                    Toast.makeText(getApplicationContext(), "please fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void checkPhoneNumber() {

        userPhoneEdtieText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 13) {
                    doneButton.setVisibility(View.VISIBLE);
                } else {
                    doneButton.setVisibility(View.GONE);
                }
            }
        });

    }

    private boolean checkIsVaildEmail(String emailAddress) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (emailAddress.isEmpty()) {
            userEmailEdtieText.setError("Invalid Email");
            return false;
        } else {
            if (emailAddress.trim().matches(emailPattern)) {
                return true;
            } else {
                userEmailEdtieText.setError("Invalid Email");

                return false;
            }
            //
        }

    }

    public boolean checkValidUserName(String Name) {
        String regx = "^[a-zA-Z \\s]+$";
        if (Name.isEmpty()) {
            userNameEdtieText.setError("Invalid Name");
            return false;
        } else {
            if (Name.trim().matches(regx)) {
                return true;
            } else {
                userNameEdtieText.setError("Invalid Name");

                return false;
            }
        }

    }
}
