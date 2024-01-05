package com.example.meetmi;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;

import ModelClass.Users;

public class FireStoreSetUp
{
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public FireStoreSetUp()
    {
        Users user = new Users(
                "user1", // username
                "pass", // password
                null, // avatar can be null if it's allowed in your class design
                0.0, // latitude, assuming you want to start with 0.0
                0.0, // longitude, assuming you want to start with 0.0
                null, // friends list can be null if it's allowed in your class design
                null, // photoFrameId can be null if it's allowed in your class design
                null, // newsfeed can be null if it's allowed in your class design
                "test" // nickname
        );

    // Now use the getters to populate the Map
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", user.getUsername());
        userMap.put("password", user.getPassword());
        userMap.put("avatar", user.getAvatar());
        userMap.put("latitude", user.getLatitude());
        userMap.put("longitude", user.getLongitude());
        userMap.put("friends", user.getFriends()); // This will be null in this case
        userMap.put("photoFrameId", user.getPhotoFrameId()); // This will be null in this case
        userMap.put("newsfeed", user.getNewsfeed()); // This will be null in this case
        userMap.put("nickname", user.getNickname());

    //add to the database
        db.collection("users")
                .add(userMap)
                .addOnSuccessListener(documentReference -> {
                    // Document was added successfully
                    Log.d("Firestore", "DocumentSnapshot added with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                    Log.w("Firestore", "Error adding document", e);
                });
    }


}
