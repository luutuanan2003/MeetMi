package com.example.meetmi;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.content.Intent;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import ModelClass.Posts;


public class FeedActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        getWindow().setStatusBarColor(Color.parseColor("#1F1F1F"));

        Intent serviceIntent = new Intent(this, ProximityService.class);
        startService(serviceIntent);

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void post_toFeed() {
        String nickname = "";
        String photo = ""; // generate or leave empty
        String video = ""; // generate or leave empty

        // Validation here for input values

        // Initialize other fields with default or empty values
        String avatar = ""; // default or empty
        if (!photo.isEmpty())
        {
            video = null;
        }
        else
        {
            photo = null;
        }

        String caption = "";
        String dateTime = "";
        List <String> comments = new ArrayList<>();
        int reaction = 0;

        // Creating post object
        Posts post = new Posts(nickname,avatar,photo, video, caption,dateTime,comments,reaction);

        // Saving to Firebase
        mDatabase.child("posts").push().setValue(post);
    }
}
