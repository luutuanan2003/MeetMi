package com.example.meetmi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.meetmi.customAdapter.UsersAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity implements UsersAdapter.OnAddFriendListener {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private EditText searchField;
    private Button searchButton;
    private RecyclerView searchResultsRecyclerView;
    private UsersAdapter usersAdapter;

    private List<Map.Entry<String, String>> friends;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchField = findViewById(R.id.search_field);
        searchButton = findViewById(R.id.search_button);
        searchResultsRecyclerView = findViewById(R.id.search_results_recyclerview);
        usersAdapter = new UsersAdapter(friends, this, this);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchResultsRecyclerView.setAdapter(usersAdapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = searchField.getText().toString();
                if (!searchText.isEmpty()) {
                    searchUsersByNickname(searchText);
                }
            }
        });
    }

    private void searchUsersByNickname(String nickname) {
        Query query = databaseReference.child("users").orderByChild("nickname").equalTo(nickname);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Users> userList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Users user = snapshot.getValue(Users.class);
                    userList.add(user);
                }
                usersAdapter.updateData(userList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error
            }
        });
    }

    @Override
    public void onAddFriend(Users user) {
        // Assuming 'user' object contains email and nickname fields
        String userEmail = user.getEmail();
        String userNickname = user.getNickname();

        // Reference to the Firebase Realtime Database
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Creating a unique ID for the user (if needed)
        String userId = usersRef.push().getKey();

        // Creating a HashMap to store user data
        HashMap<String, String> userData = new HashMap<>();
        userData.put("email", userEmail);
        userData.put("nickname", userNickname);

        // Adding or updating the user data in Firebase Realtime Database
        usersRef.child(userId).setValue(userData)
                .addOnSuccessListener(aVoid -> {
                    // Handle success (e.g., display a message)
                    Toast.makeText(SearchActivity.this, "User added to database", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle failure (e.g., display an error message)
                    Toast.makeText(SearchActivity.this, "Failed to add user to database", Toast.LENGTH_SHORT).show();
                });
}

}
