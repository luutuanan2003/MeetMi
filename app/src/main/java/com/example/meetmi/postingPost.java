package com.example.meetmi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import ModelClass.Posts;
import ModelClass.UserCallback;
import ModelClass.UserManager;

public class postingPost extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting_post);

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void uploadImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            ImageView photoView = findViewById(R.id.photoView);
            photoView.setImageURI(imageUri);
        }
    }

    private void post_toFeed() {
        // Initialize your variables here
        String photo = ""; // Initialize photo
        String video = ""; // Initialize video
        String caption = ""; // Initialize caption
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
                } else {
                    // Handle the case where the user is not found
                    // Possibly show an error message or log this event
                }
            }
        });
    }


}