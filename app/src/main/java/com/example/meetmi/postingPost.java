package com.example.meetmi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.meetmi.customAdapter.GalleryAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import ModelClass.Posts;
import ModelClass.UserCallback;
import ModelClass.UserManager;
import android.app.AlertDialog;


public class postingPost extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private DatabaseReference mDatabase;
    private EditText CaptionField;

    private Button postButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting_post);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        CaptionField = findViewById(R.id.captionField);
        postButton = findViewById(R.id.postButton);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post_toFeed();
            }
        });
    }



    public void uploadImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // what is the difference between url and uri wtf?
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // get the image from the device
            Uri imageUri = data.getData();
            // add of the image url in the list
            List<Uri> imageUrls = new ArrayList<>();
            imageUrls.add(imageUri);

            GridView galleryGridView = findViewById(R.id.gallery_posting);

            // pass the image uri into the adapter and then use that adapter to bring the image into the gridview
            GalleryAdapter GalleryAdapter = new GalleryAdapter(this, imageUrls);
            galleryGridView.setAdapter(GalleryAdapter);
        }
    }

//    old code for getting image from the device and put to the app
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            Uri imageUri = data.getData();
//            ImageView photoView = findViewById(R.id.photoView);
//            photoView.setImageURI(imageUri);
//        }
//    }

    private void post_toFeed() {
        // Initialize your variables here
        String photo = ""; // Initialize photo
        String video = ""; // Initialize video
        String caption = CaptionField.getText().toString().trim();
        String dateTime = ""; // Initialize dateTime

        // Initialize other required fields
        List<String> comments = new ArrayList<>();
        int reaction = 0;

        UserManager.getCurrentUserDetail(new UserCallback() {
            @Override
            public void onCallback(Users user) {
                if (user != null) {
                    // Now that we have the user, we can create and post
                    String nickname = user.getNickname();
                    String avatar = user.getAvatar(); // Assuming getAvatar() method exists

                    // Creating post object
                    Posts post = new Posts(nickname, avatar, photo, video, caption, dateTime, comments, reaction);

                    // Saving to Firebase
                    mDatabase.child("posts").push().setValue(post);
                    showConfirmationDialog();
                } else {
                    showUserNotFoundDialog();

                }
            }
        });

    }
    private void showUserNotFoundDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("User Not Found");
        builder.setMessage("The user you are looking for does not exist.");
        builder.setPositiveButton("OK", (dialog, id) -> {
            dialog.dismiss();
        });
        builder.setNegativeButton("Cancel", (dialog, id) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showConfirmationDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage("Post has been posted.")
                .setCancelable(false)
                .setPositiveButton("Back to Feed", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Start the Sign In activity
                        Intent signInIntent = new Intent(postingPost.this, FeedActivity.class);
                        startActivity(signInIntent);
                        finish();
                    }
                });
        androidx.appcompat.app.AlertDialog alert = builder.create();
        alert.show();
    }

}