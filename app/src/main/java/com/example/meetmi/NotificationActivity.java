package com.example.meetmi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.meetmi.customAdapter.FeedPostAdapter;
import com.example.meetmi.customAdapter.NotificationAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import ModelClass.Notification;
import ModelClass.Posts;
import ModelClass.UserManager;


public class NotificationActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private RecyclerView notiRecyclerView;
    private NotificationAdapter NotiAdapter;
    private List<Notification> notiList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        getWindow().setStatusBarColor(Color.parseColor("#1F1F1F"));

        mDatabase = FirebaseDatabase.getInstance().getReference();
        notiRecyclerView = findViewById(R.id.notiRecyclerView);
        notiRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        NotiAdapter = new NotificationAdapter(this,notiList);
        notiRecyclerView.setAdapter(NotiAdapter);

        UserManager.getCurrentUserNotifications(new UserManager.NotificationCallback() {
            @Override
            public void onNotificationsReceived(List<Notification> notifications) {
                notiList.clear();
                notiList.addAll(notifications);
                NotiAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {

            }
        });

    }

    public void goto_feed(View view) {
        Intent intent = new Intent(NotificationActivity.this,FeedActivity.class);
        startActivity(intent);
        finish();
    }

    public void goto_postingPost(View view) {
        Intent intent = new Intent(NotificationActivity.this,postingPost.class);
        startActivity(intent);
        finish();
    }



    public void goto_profile(View view) {
        Intent intent = new Intent(NotificationActivity.this,ProfileActivity.class);
        startActivity(intent);
        finish();
    }
}
