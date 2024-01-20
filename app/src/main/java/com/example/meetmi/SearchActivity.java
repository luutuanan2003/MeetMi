package com.example.meetmi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;


import com.example.meetmi.customAdapter.FriendsAdapter;
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
import java.util.Map;

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
        UserManager.getCurrentUserDetail(new UserManager.UserCallback() {
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
    public void onAddFriendClicked(String friendNickname) {
        if (loggedInEmail != null && !loggedInEmail.isEmpty()) {
            addFriendByNickname(loggedInEmail, friendNickname);
        }
        // else handle the case where loggedInUsername is not available
    }

    private void addFriendByNickname(String loggedInEmail, String friendNickname) {
        DatabaseReference usersRef = mDatabase.child("users");

        // First, find the logged-in user's key
        usersRef.orderByChild("email").equalTo(loggedInEmail)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                            DataSnapshot userSnapshot = dataSnapshot.getChildren().iterator().next();
                            String loggedInUserKey = userSnapshot.getKey();

                            // Now, find the friend's email by their nickname
                            usersRef.orderByChild("nickname").equalTo(friendNickname)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                                                DataSnapshot friendSnapshot = dataSnapshot.getChildren().iterator().next();
                                                String friendEmail = friendSnapshot.child("email").getValue(String.class);

                                                // Add the friend's email to the "friends" node of the logged-in user
                                                DatabaseReference friendsRef = usersRef.child(loggedInUserKey).child("friends");
                                                friendsRef.child(friendNickname).setValue(friendEmail);
                                                Toast.makeText(SearchActivity.this, "Friend has been added", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Log.e("SearchActivity", "Friend not found in the database.");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            Log.e("SearchActivity", "Database error: " + databaseError.getMessage());
                                        }
                                    });
                        } else {
                            Log.e("SearchActivity", "Logged-in user not found in the database.");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("SearchActivity", "Database error: " + databaseError.getMessage());
                    }
                });
    }


    public void showFriendsDialog(View view) {
        UserManager.getCurrentUserDetail(new UserManager.UserCallback() {
            @Override
            public void onCallback(Users currentUser) {
                if (currentUser != null) {
                    List<String> friendsList = new ArrayList<>(currentUser.getFriends().keySet());
                    showFriendsListDialog(friendsList);
                } else {
                    Toast.makeText(SearchActivity.this, "You have no friends added.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void showFriendsListDialog(List<String> friendsList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_friends_list, null);
        RecyclerView recyclerView = dialogView.findViewById(R.id.rv_friends_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FriendsAdapter adapter = new FriendsAdapter(friendsList);
        recyclerView.setAdapter(adapter);

        builder.setView(dialogView)
                .setPositiveButton("Close", (dialog, id) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }




    public void backtomain(View view) {
        Intent intent = new Intent(SearchActivity.this,FeedActivity.class);
        startActivity(intent);
        finish();
    }




    // Add other necessary methods for SearchActivity
}
