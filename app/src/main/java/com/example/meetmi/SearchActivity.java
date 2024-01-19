package com.example.meetmi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.example.meetmi.customAdapter.UsersAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;


import ModelClass.Posts;
import ModelClass.UserCallback;
import ModelClass.UserManager;
import com.example.meetmi.Users;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements UsersAdapter.OnItemClickListener {

    private DatabaseReference mDatabase;
    private EditText searchField;
    private Button searchButton;
    private RecyclerView searchResultsRecyclerView;
    private UsersAdapter usersAdapter;
    private String loggedInEmail;
    private List<Users> userList = new ArrayList<>();
    private String currentusername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        searchField = findViewById(R.id.search_field);
        searchButton = findViewById(R.id.search_button);
        searchResultsRecyclerView = findViewById(R.id.search_results_recyclerview);

        usersAdapter = new UsersAdapter(this, new ArrayList<>(), this);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchResultsRecyclerView.setAdapter(usersAdapter);

        searchButton.setOnClickListener(v -> {
            String searchText = searchField.getText().toString();
            if (!searchText.isEmpty()) {
                searchUsersByNickname(searchText);
            }
        });

        getCurrentLoggedInEmail();
    }
    private void searchUsersByNickname(String nickname) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        usersRef.orderByChild("nickname").equalTo(nickname)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Users> userList = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Users user = snapshot.getValue(Users.class);
                            userList.add(user);
                        }
                        usersAdapter.updateData(userList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("SearchActivity", "Error while searching users", databaseError.toException());
                    }
                });
    }

    private void getCurrentLoggedInEmail() {
        UserManager.getCurrentUserDetail(new UserCallback() {
            @Override
            public void onCallback(Users user) {
                if (user != null) {
                    loggedInEmail = user.getEmail();
                    currentusername = user.getNickname();
                } else {
                    // Handle case where user details are not available
                }
            }
        });
    }

    @Override
    public void onAddFriendClicked(String friendUsername) {
        if (loggedInEmail != null && !loggedInEmail.isEmpty()) {
            addFriendByUsername(loggedInEmail, friendUsername);
        }
        // else handle the case where loggedInUsername is not available
    }

    private void addFriendByUsername(String loggedInEmail, String friendUsername) {
        DatabaseReference usersRef = mDatabase.child("users");

        // Query the users by email to find the logged-in user's unique key
        usersRef.orderByChild("email").equalTo(loggedInEmail)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                            // Assuming email is unique and can only have one entry, get the unique key
                            DataSnapshot userSnapshot = dataSnapshot.getChildren().iterator().next();
                            String loggedInUserKey = userSnapshot.getKey();

                            // Now we have the unique key, add the friendUsername to the "friends" path
                            DatabaseReference friendsRef = usersRef.child(loggedInUserKey).child("friends");
                            friendsRef.child(friendUsername).setValue("Friend of" + " " + currentusername); // Using 'true' as a placeholder value
                            Toast.makeText(SearchActivity.this, "Friend has been added", Toast.LENGTH_SHORT).show();
                        } else {
                            // Handle the case where the logged-in user is not found
                            Log.e("SearchActivity", "Logged-in user not found in the database.");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle database error
                        Log.e("SearchActivity", "Database error: " + databaseError.getMessage());
                    }
                });
    }

    public void backtomain(View view) {
        Intent intent = new Intent(SearchActivity.this,FeedActivity.class);
        startActivity(intent);
        finish();
    }




    // Add other necessary methods for SearchActivity
}
