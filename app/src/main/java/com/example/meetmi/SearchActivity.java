package com.example.meetmi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.meetmi.customAdapter.UsersAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private EditText searchField;
    private Button searchButton;
    private RecyclerView searchResultsRecyclerView;
    private UsersAdapter usersAdapter;
    private Button addbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchField = findViewById(R.id.search_field);
        searchButton = findViewById(R.id.search_button);
        searchResultsRecyclerView = findViewById(R.id.search_results_recyclerview);

        usersAdapter = new UsersAdapter(this, new ArrayList<>());
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

            public void addbutton(View view) {


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
                updateUserList(userList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error
            }
        });
    }

    private void updateUserList(List<Users> userList) {
        usersAdapter.updateData(userList);
    }

    // Function to add a friend's email to the current user's 'friends' list in Firebase
    public void addFriendEmail(String currentUseremail, String friendEmail) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");

        // Assuming 'friends' is a list of emails, if not, this logic will need to be adjusted accordingly.
        userRef.child(currentUseremail).child("friends").push().setValue(friendEmail)
                .addOnSuccessListener(aVoid -> {
                    // Friend added successfully, handle this case
                    Log.d("AddFriend", "Friend email added successfully!");
                })
                .addOnFailureListener(e -> {
                    // Failed to add friend, handle this case
                    Log.d("AddFriend", "Failed to add friend email: " + e.getMessage());
                });
    }

}
