package com.example.meetmi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.example.meetmi.customAdapter.GalleryAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ModelClass.Posts;
import ModelClass.UserCallback;
import ModelClass.UserManager;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.AlertDialog;
import android.widget.TextView;
import android.widget.Toast;


public class postingPost extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private DatabaseReference mDatabase;
    private EditText CaptionField;
    private StorageReference mStorageRef;
    private Uri selectedImageUri;
    private List<String> imageUrlsString = new ArrayList<>();
    private List<Uri> imageUrls = new ArrayList<>();
    private CircleImageView user_Avatar;
    private TextView user_Nickname;
    private Button postButton,backButton;
    private RecyclerView galleryRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting_post);

        Log.d("red", "hehehehehehe");

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        CaptionField = findViewById(R.id.captionField);
        postButton = findViewById(R.id.postButton);
        backButton  = findViewById(R.id.back_to_mainPP);
        checkCaptionnotNull();

        //load user nickname + avatar
        user_Avatar = findViewById(R.id.userAvatar_postingPost);
        user_Nickname = findViewById(R.id.nickName_postingPost);

        checkCaptionHandler.post(checkCaptionRunnable);
        UserManager.getCurrentUserDetail(new UserCallback() {
            @Override
            public void onCallback(Users user) {
                if (user != null) {
                    String nickname = user.getNickname();
                    String avatar = user.getAvatar().toString(); // This retrieves the avatar URL from Firebase
                    user_Nickname.setText(nickname);
                    Log.d("FirebaseDebug", "User Retrieved: " + user.getNickname());

                    // Check if the avatar URL is not null and not empty
                    if (avatar != null && !avatar.isEmpty()) {
                        Picasso.get()
                                .load(avatar)
                                .into(user_Avatar);
                    } else {
                        // Load a default image if the avatar URL is null or empty
                        Picasso.get()
                                .load(R.drawable.sampleavatar) // Replace with your default avatar drawable
                                .into(user_Avatar);
                    }
                } else {
                    showUserNotFoundDialog();
                }
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post_toFeed();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(postingPost.this ,FeedActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // destroy the loop in the post for the app to run smoothly
    @Override
    protected void onDestroy() {
        super.onDestroy();
        checkCaptionHandler.removeCallbacks(checkCaptionRunnable);
    }


    //check for caption to not be null and have the post button appear
    private Handler checkCaptionHandler = new Handler(Looper.getMainLooper());
    private Runnable checkCaptionRunnable = new Runnable() {
        @Override
        public void run() {
            checkCaptionnotNull();
            // Schedule the next execution after a delay
            checkCaptionHandler.postDelayed(this, 1000); // Check every 1000 milliseconds (1 second)
        }
    };
    public void checkCaptionnotNull() {
        String caption = CaptionField.getText().toString();
        if (!caption.equals("")) {
            setVisible(R.id.postButton, true);
        } else {
            setVisible(R.id.postButton, false);
        }
    }

    public void uploadImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Allow multiple selections
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            imageUrls.clear(); // Clear the list to prevent duplicates if this is called again
            if (data.getClipData() != null) {
                // Multiple images selected
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    imageUrls.add(imageUri); // Add each URI to the list
                }
            } else if (data.getData() != null) {
                // Single image selected
                Uri imageUri = data.getData();
                imageUrls.add(imageUri); // Add single URI to the list
            }

            uploadImagesToFirebaseStorage(imageUrls); // Call the upload method
            updateGalleryRecyclerView(imageUrls); // Update UI if necessary
        }
    }

    private void updateGalleryRecyclerView(List<Uri> imageUrls) {
        RecyclerView galleryRecyclerView = findViewById(R.id.gallery_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        galleryRecyclerView.setLayoutManager(layoutManager);
        GalleryAdapter galleryAdapter = new GalleryAdapter(this, imageUrls);
        galleryRecyclerView.setAdapter(galleryAdapter);
    }



    private void post_toFeed() {
        // Initialize your variables here

        String video = ""; // Initialize video
        String caption = CaptionField.getText().toString().trim();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateTime = now.format(formatter);

        // Initialize other required fields
        List<String> comments = new ArrayList<>();
        int reaction = 0;

        UserManager.getCurrentUserDetail(new UserCallback() {
            @Override
            public void onCallback(Users user) {
                if (user != null) {
                    // Now that we have the user, we can create and post
                    String nickname = user.getNickname();
                    String avatar = user.getAvatar(); // Assuming getAvatar() method exists

                    // Creating post object
                    Posts post = new Posts(nickname, avatar, imageUrlsString, video, caption, dateTime, comments, reaction);

                    // Saving to Firebase
                    mDatabase.child("posts").push().setValue(post);
                    showConfirmationDialog();
                } else {
                    showUserNotFoundDialog();

                }
            }
        });

    }
    private void showUserNotFoundDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("User Not Found");
        builder.setMessage("The user you are looking for does not exist.");
        builder.setPositiveButton("OK", (dialog, id) -> {
            dialog.dismiss();
        });
        builder.setNegativeButton("Cancel", (dialog, id) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showConfirmationDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage("Post has been posted.")
                .setCancelable(false)
                .setPositiveButton("Back to Feed", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Start the Sign In activity
                        Intent signInIntent = new Intent(postingPost.this, FeedActivity.class);
                        startActivity(signInIntent);
                        finish();
                    }
                });
        androidx.appcompat.app.AlertDialog alert = builder.create();
        alert.show();
    }

    public void goto_feed(View view) {
        Intent intent = new Intent(postingPost.this,FeedActivity.class);
        startActivity(intent);
        finish();
    }

    public void goto_notification(View view) {
        Intent intent = new Intent(postingPost.this,NotificationActivity.class);
        startActivity(intent);
        finish();
    }

    public void goto_profile(View view) {
        Intent intent = new Intent(postingPost.this,ProfileActivity.class);
        startActivity(intent);
        finish();
    }


    private void setVisible(int id, boolean isVisible){
        View aView = findViewById(id);
        if (isVisible)
            aView.setVisibility(View.VISIBLE);
        else
            aView.setVisibility(View.INVISIBLE);
    }

    private void uploadImagesToFirebaseStorage(List<Uri> imageUris) {
        for (Uri imageUri : imageUris) {
            if (imageUri != null) {
                // Create a unique file name for each image
                StorageReference fileRef = mStorageRef.child("avatars/" + UUID.randomUUID().toString() + ".jpg");

                // Upload each file to Firebase Storage
                fileRef.putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get the download URL for each image
                                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri downloadUri) {
                                        // Call a method to handle the download URL for each image
                                        getImageUrl(downloadUri.toString());
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle unsuccessful uploads for each image
                                Toast.makeText(postingPost.this, "Upload failed for image: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }

    private void getImageUrl (String avatarUrl)
    {
        imageUrlsString.add(avatarUrl);
    }
}