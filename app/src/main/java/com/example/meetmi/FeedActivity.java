package com.example.meetmi;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.content.Intent;
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
    }


    public void goto_postingPost(View view) {
        Intent intent = new Intent(FeedActivity.this,postingPost.class);
        startActivity(intent);
    }
}
