package com.example.meetmi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.meetmi.customAdapter.CommentAdapter;
import com.example.meetmi.customAdapter.FeedPostAdapter;
import com.example.meetmi.customAdapter.PostAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ModelClass.Notification;
import ModelClass.Posts;
import ModelClass.UserCallback;
import ModelClass.UserManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import ModelClass.UserManager.FriendsCallback;



public class FeedActivity extends AppCompatActivity implements FeedPostAdapter.OnPostInteractionListener {
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private DatabaseReference mDatabase;
    private RecyclerView postsRecyclerView;
    private FeedPostAdapter feedPostAdapter;
    private ImageView commentSection,reactionSection;
    private List<Posts> postList = new ArrayList<>();
    private List<String> friendEmails;

    private String currentUserNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        getWindow().setStatusBarColor(Color.parseColor("#1F1F1F"));

        Intent serviceIntent = new Intent(this, ProximityService.class);
        startService(serviceIntent);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        postsRecyclerView = findViewById(R.id.postsRecyclerView);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        feedPostAdapter = new FeedPostAdapter(this, postList,this);
        postsRecyclerView.setAdapter(feedPostAdapter);
        friendEmails = new ArrayList<>();


        fetchCurrentUserAndPosts();


        checkLocationPermissionAndStartService();

    }

    private void fetchCurrentUserAndPosts() {
        UserManager.getCurrentUserDetail(new UserManager.UserCallback() {
            @Override
            public void onCallback(Users user) {
                if (user != null) {
                    currentUserNickname = user.getNickname();
                    Log.d("CurrentNickname", "Current User Nickname: " + currentUserNickname);
                    // Fetch posts for the current user
                    fetchPosts(currentUserNickname);
                    // Fetch posts for the user's friends
                    fetchFriendsPosts();
                } else {
                    Log.e("FeedActivity", "User details are not available.");
                }
            }
        });
    }

    private void fetchPosts(String nickname) {
        Log.d("FeedActivity", "Fetching posts for: " + nickname);
        mDatabase.child("posts").orderByChild("nickname").equalTo(nickname)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            Log.d("FeedActivity", "No posts found for: " + nickname);
                            return;
                        }
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Posts post = postSnapshot.getValue(Posts.class);
                            if (post != null) {
                                postList.add(post);
                            }
                        }
                        feedPostAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("FeedActivity", "Database error: " + databaseError.getMessage());
                    }
                });
    }

    private void fetchFriendsPosts() {
        // Assuming currentUserNickname is already set
        mDatabase.child("users").child(currentUserNickname).child("friends")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot friendSnapshot : dataSnapshot.getChildren()) {
                            String friendEmail = friendSnapshot.getValue(String.class);
                            if (friendEmail != null && !friendEmail.isEmpty()) {
                                fetchPostsByEmail(friendEmail);
                            } else {
                                Log.e("FeedActivity", "A friend's email is null or empty.");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("FirebaseCheck", "Failed to read friends: " + databaseError.getMessage());
                    }
                });
    }

    private void fetchPostsByEmail(String email) {
        mDatabase.child("posts").orderByChild("user_Email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            Log.d("FeedActivity", "No posts found for email: " + email);
                            return;
                        }
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Posts post = postSnapshot.getValue(Posts.class);
                            if (post != null) {
                                postList.add(post);
                            }
                        }
                        Collections.sort(postList, new Comparator<Posts>() {
                            @Override
                            public int compare(Posts post1, Posts post2) {
                                // Example sorting: by date, replace with your own logic
                                return post1.getDateTime().compareTo(post2.getDateTime());
                            }
                        });
                        feedPostAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("FeedActivity", "Database error: " + databaseError.getMessage());
                    }
                });
    }




    private void checkLocationPermissionAndStartService() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            // Permission is granted, start the service
            startProximityService();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted, start the service
                startProximityService();
            } else {
                // Permission denied, handle the case (e.g., show a message to the user)
                Toast.makeText(this, "Location permission is required for this feature", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void startProximityService() {
        Intent serviceIntent = new Intent(this, ProximityService.class);
        // add any extra data to serviceIntent if needed
        startService(serviceIntent);
    }
    public void goto_google_search(View view) {
        Intent intent = new Intent(FeedActivity.this,SearchActivity.class);
        startActivity(intent);
        finish();
    }

    public void goto_postingPost(View view) {
        Intent intent = new Intent(FeedActivity.this,postingPost.class);
        startActivity(intent);
        finish();
    }


    public void goto_profile(View view) {
        Intent intent = new Intent(FeedActivity.this,ProfileActivity.class);
        startActivity(intent);
        finish();
    }
    public void goto_notification(View view) {
        Intent intent = new Intent(FeedActivity.this,NotificationActivity.class);
        startActivity(intent);
        finish();
    }



    @Override
    public void onCommentClick(int position) {
        // Retrieve the post object
        Posts post = postList.get(position);

        Map<String, String> commentss = post.getComments();
        if (commentss == null) {
            commentss = new HashMap<>(); // Initialize to empty if null
        }

        // Create and show the comments list dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(FeedActivity.this);
        builder.setTitle("Comments");

        // Inflate and set the layout for the dialog containing the RecyclerView
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_comments_list, null);
        RecyclerView commentsRecyclerView = dialogView.findViewById(R.id.rv_comments); // RecyclerView in your dialog_comments_list.xml
        // Initialize RecyclerView with a layout manager and adapter (Assuming you have a CommentsAdapter)
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        CommentAdapter commentsAdapter = new CommentAdapter(post.getComments()); // pass the comments from the post
        commentsRecyclerView.setAdapter(commentsAdapter);

        builder.setView(dialogView);

        // "Comment Your Thought" button to show the comment input dialog
        builder.setPositiveButton("Comment Your Thought", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                UserManager.getCurrentUserDetail(new UserManager.UserCallback() {
                    @Override
                    public void onCallback(Users user) {
                        if (user != null) {
                            // Now you have the user's details, including nickname
                            String currentUserNickname = user.getNickname();

                            // Proceed to show the comments dialog with the fetched nickname
                            showCommentInputDialog(post, currentUserNickname);
                        } else {
                            // Handle the case where user details are not found
                            Toast.makeText(FeedActivity.this, "User details not found.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        // Cancel button to dismiss the comments list dialog
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }


    @Override
    public void onReactionClick(int position) {
        // Retrieve the post object
        Posts post = postList.get(position);

        // Increment the reaction count in the database
        mDatabase.child("posts").child(post.getKeyID()).child("reaction").runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Integer currentReactions = mutableData.getValue(Integer.class);
                if (currentReactions == null) {
                    mutableData.setValue(1);
                } else {
                    mutableData.setValue(currentReactions + 1);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                // Once the reaction count has been updated, create a notification
                DatabaseReference notificationRef = mDatabase.child("notifications").push();
                notificationRef.setValue(new Notification(
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        post.getNickname(),
                        post.getUser_Email(),
                        post.getAvatar(),
                        "0", // isComment
                        "1" , // isReaction
                        "",
                        post.getUser_Email()
                ));
                Log.d("CheckUserEmail" , post.getUser_Email());

                // Notify the user that the reaction has been recorded
                Toast.makeText(FeedActivity.this, "Reacted!", Toast.LENGTH_SHORT).show();
            }
        });
    }




    public void showCommentInputDialog(Posts post, String currentUserNickname) {
        // Create and show the comment dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(FeedActivity.this);
        builder.setTitle("Comment What You Think:");

        // Inflate and set the layout for the dialog
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_comment, null);
        final EditText commentInput = dialogView.findViewById(R.id.commentSection);
        builder.setView(dialogView);

        // Set up the button to submit the comment
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String comment = commentInput.getText().toString().trim();
                if (!comment.isEmpty()) {
                    // Add comment to the post's comments section in the Realtime Database

                    mDatabase.child("posts").child(post.getKeyID()).child("comments")
                            .push().setValue(currentUserNickname + ": " + comment);


                    // Create a notification document in the Realtime Database
                    DatabaseReference notificationRef = mDatabase.child("notifications").push();
                    notificationRef.setValue(new Notification(
                            post.getDateTime(),
                            post.getNickname(),
                            post.getUser_Email(),
                            post.getAvatar(),
                            "1", // isComment
                            "0", // isReaction
                            comment,
                            post.getUser_Email()
                    ));
                    Log.d("CheckUserEmail" , post.getUser_Email());
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }


}