package com.example.meetmi;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;


public class NotificationActivity extends AppCompatActivity {
//    private ListView listView;
//    private UserAdapter adapter;
//    private List<Users> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        getWindow().setStatusBarColor(Color.parseColor("#1F1F1F"));

//        the flow of the notification list
//        listView = findViewById(R.id.listView);
//
//        // Populate the user list
//        userList = new ArrayList<>();
//        userList.add(new User("image_url_1", "User 1"));
//        userList.add(new User("image_url_2", "User 2"));
//        // Add more users...
//
//        adapter = new UserAdapter(this, userList);
//        listView.setAdapter(adapter);
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
