package com.example.meetmi;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.content.Intent;


public class NotificationActivity extends AppCompatActivity {
//    private ListView listView;
//    private UserAdapter adapter;
//    private List<Users> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        getWindow().setStatusBarColor(Color.parseColor("#1F1F1F"));
        Intent serviceIntent = new Intent(this, ProximityService.class);
        startService(serviceIntent);

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
}
