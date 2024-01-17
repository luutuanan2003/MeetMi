package com.example.meetmi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.meetmi.customAdapter.FeedPostAdapter;
import com.example.meetmi.customAdapter.PostAdapter;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class FeedActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private RecyclerView postsRecyclerView;
    private FeedPostAdapter feedPostAdapter;
    private ImageView commentSection,reactionSection;
    private List<Posts> postList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        getWindow().setStatusBarColor(Color.parseColor("#1F1F1F"));

        Intent serviceIntent = new Intent(this, ProximityService.class);
        startService(serviceIntent);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        postsRecyclerView = findViewById(R.id.postsRecyclerView);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        feedPostAdapter = new FeedPostAdapter(this, postList);
        postsRecyclerView.setAdapter(feedPostAdapter);
        commentSection = findViewById(R.id.reactionB);
        UserManager.getUserPosts(new UserManager.PostsCallback() {
            @Override
            public void onPostsReceived(List<Posts> posts) {
                postList.clear();
                postList.addAll(posts);
                feedPostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                Log.e("FirebaseCheck", error);
            }
        });



    }


    public void goto_postingPost(View view) {
        Intent intent = new Intent(FeedActivity.this,postingPost.class);
        startActivity(intent);
        finish();
    }


    public void goto_profile(View view) {
        Intent intent = new Intent(FeedActivity.this,ProfileActivity.class);
        startActivity(intent);
        finish();
    }
    public void goto_notification(View view) {
        Intent intent = new Intent(FeedActivity.this,NotificationActivity.class);
        startActivity(intent);
        finish();
    }


}