package com.example.meetmi;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FireStoreSetUp
{
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Create a new user with a first and last name
    Map<String, Object> user = new HashMap<>();
    user.put("first", "Ada");
    user.put("last", "Lovelace");
    user.put("born", 1815);
}
