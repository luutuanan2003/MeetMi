package com.example.meetmi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import ModelClass.Posts;
import ModelClass.UserCallback;
import ModelClass.UserManager;


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

        UserManager.getUserPosts(new UserManager.PostsCallback() {
            @Override
            public void onPostsReceived(List<Posts> posts) {
                // Handle the list of posts for the current user
                for (Posts post : posts) {
                    Log.d("FirebaseDebug", "caption:" + post.getCaption());
                }
            }

            @Override
            public void onError(String error) {
                // Handle the error, e.g., show a message to the user
                Log.e("FirebaseCheck", error);
            }
        });

    }


    public void goto_postingPost(View view) {
        Intent intent = new Intent(FeedActivity.this,postingPost.class);
        startActivity(intent);
        finish();
    }


}