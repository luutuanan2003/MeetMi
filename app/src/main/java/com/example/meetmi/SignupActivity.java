package com.example.meetmi;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SignupActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;


    //TODO: need to collect data from the nickname field and the button for the avatar as well
    private String selectedImage;
    private EditText usernameField, passwordField, re_enter_passwordField, emailField, nickNameField;
    private Button signupButton;
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getWindow().setStatusBarColor(Color.parseColor("#1F1F1F"));

        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        // Initialize fields
        nickNameField = findViewById(R.id.nicknameField_SU);
        usernameField = findViewById(R.id.usernameField_SU);
        passwordField = findViewById(R.id.passwordField);
        re_enter_passwordField = findViewById(R.id.repasswordField);
        signupButton = findViewById(R.id.signupButtonLogin);
        emailField = findViewById(R.id.user_emailField);
        setVisible(R.id.errorTextSignup,false);




        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatePassword();
            }
        });
    }


    // Method for validate the correct password input before running the main method "registerUser"
    private void validatePassword(){
        String password = passwordField.getText().toString().trim();
        String re_enter_password = re_enter_passwordField.getText().toString().trim();
        if(!password.equals(re_enter_password)){
            setVisible(R.id.errorTextSignup,true);
        } else {
            registerUser();
        }
    }

    private void registerUser() {
        String email = emailField.getText().toString().trim();
        String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String nickname = nickNameField.getText().toString().trim();

        // Validation here for input values

        // Initialize other fields with default or empty values
        String avatar = selectedImage; // default or empty
        Log.d("Image URL", "image url is " + selectedImage);
        String id = ""; // generate or leave empty
        double latitude = 0; // default value
        double longitude = 0; // default value
        List<String> friends = new ArrayList<>(); // empty list
        String photoFrameId = ""; // default or empty
        List<String> newsfeed = new ArrayList<>(); // empty list


        // Creating user object
        Users user = new Users(email,username, password, avatar, id, latitude, longitude,
                friends, photoFrameId, newsfeed, nickname);

        // Saving to Firebase
        mDatabase.child("users").push().setValue(user);
        showConfirmationDialog();
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Account has been created successfully.")
                .setCancelable(false)
                .setPositiveButton("Back to Sign In", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Start the Sign In activity
                        Intent signInIntent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(signInIntent);
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    // Helper method to replace fragments
    private void displayFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.signup_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setVisible(int id, boolean isVisible){
        View aView = findViewById(id);
        if (isVisible)
            aView.setVisibility(View.VISIBLE);
        else
            aView.setVisibility(View.INVISIBLE);
    }

    public void uploadImage_signup(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            uploadImageToFirebaseStorage(imageUri);
            ImageView photoView = findViewById(R.id.userAvatar_signup);
//            photoView.setImageURI(imageUri);
            Glide.with(this)
                    .load(imageUri)
                    .apply(new RequestOptions().circleCrop())
                    .into(photoView);

        }
    }

    private void uploadImageToFirebaseStorage(Uri imageUri) {
        if (imageUri != null) {
            // Create a reference to 'avatars/uniqueId.jpg'
            StorageReference fileRef = mStorageRef.child("avatars/" + UUID.randomUUID().toString() + ".jpg");

            // Upload the file to Firebase Storage
            fileRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get the download URL
                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUri) {
                                    getImageUrl(downloadUri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle unsuccessful uploads
                            Toast.makeText(SignupActivity.this, "Upload failed: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }



    private void getImageUrl (String avatarUrl)
    {
        selectedImage = avatarUrl;
    }
}
