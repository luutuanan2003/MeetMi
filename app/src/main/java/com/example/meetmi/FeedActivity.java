package com.example.meetmi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.google.firebase.firestore.auth.User;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import ModelClass.Notification;
import ModelClass.Posts;
import ModelClass.UserCallback;
import ModelClass.UserManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class FeedActivity extends AppCompatActivity implements FeedPostAdapter.OnPostInteractionListener {

    private DatabaseReference mDatabase;
    private RecyclerView postsRecyclerView;
    private FeedPostAdapter feedPostAdapter;
    private ImageView commentSection,reactionSection;
    private List<Posts> postList = new ArrayList<>();

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

        UserManager.getUserPosts(new UserManager.PostsCallback() {
            @Override
            public void onPostsReceived(List<Posts> posts) {
                postList.clear();
                postList.addAll(posts);
                feedPostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                Log.e("FirebaseCheck", error);
            }
        });



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
    public void goto_google_search(View view) {
        Intent intent = new Intent(FeedActivity.this,SearchActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCommentClick(int position) {
        // Retrieve the post object
        Posts post = postList.get(position);

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
                showCommentInputDialog(post);
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
                        ""
                ));

                // Notify the user that the reaction has been recorded
                Toast.makeText(FeedActivity.this, "Reacted!", Toast.LENGTH_SHORT).show();
            }
        });
    }




    public void showCommentInputDialog (Posts post)
    {
        // Create and show the comment dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(FeedActivity.this);
        builder.setTitle("Comment What You Think:");

        // Inflate and set the layout for the dialog
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_comment, null);
        final EditText commentInput = dialogView.findViewById(R.id.commentSection); // Ensure you have an EditText with this ID in your layout
        builder.setView(dialogView);

        // Set up the button to submit the comment
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String comment = commentInput.getText().toString().trim();
                if (!comment.isEmpty()) {
                    // Add comment to the post's comments section in the Realtime Database
                    mDatabase.child("posts").child(post.getKeyID()).child("comments").push().setValue(comment);

                    // Create a notification document in the Realtime Database
                    DatabaseReference notificationRef = mDatabase.child("notifications").push();
                    notificationRef.setValue(new Notification(
                            post.getDateTime(), // Use the post's datetime
                            post.getNickname(), // fromUser
                            post.getUser_Email(), //fromUserEmail
                            post.getAvatar(), // userAvatar
                            "1", // isComment
                            "0", // isReaction
                            comment
                    ));
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