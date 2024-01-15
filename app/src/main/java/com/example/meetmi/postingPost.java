package com.example.meetmi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.meetmi.customAdapter.GalleryAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ModelClass.Posts;
import ModelClass.UserCallback;
import ModelClass.UserManager;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.AlertDialog;
import android.widget.ListView;
import android.widget.TextView;


public class postingPost extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private DatabaseReference mDatabase;
    private EditText CaptionField;
    private Uri selectedImageUri;
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


        mDatabase = FirebaseDatabase.getInstance().getReference();
        CaptionField = findViewById(R.id.captionField);
        postButton = findViewById(R.id.postButton);
        backButton  = findViewById(R.id.back_to_mainPP);

        //load user nickname + avatar
        user_Avatar = findViewById(R.id.userAvatar_postingPost);
        user_Nickname = findViewById(R.id.nickName_postingPost);
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


//        galleryRecyclerView = findViewById(R.id.gallery_recycler_view);
//        galleryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

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





    public void uploadImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Allow multiple selections
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

//    on activity cu
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            selectedImageUri = data.getData();
//            imageUrls.add(selectedImageUri);
//
//            RecyclerView galleryRecyclerView = findViewById(R.id.gallery_recycler_view);
//
//            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//            galleryRecyclerView.setLayoutManager(layoutManager);
//
//            GalleryAdapter galleryAdapter = new GalleryAdapter(this, imageUrls);
//            galleryRecyclerView.setAdapter(galleryAdapter);
//        }
//    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                // Multiple images selected
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    imageUrls.add(imageUri);
                }
            } else if (data.getData() != null) {
                // Single image selected
                Uri imageUri = data.getData();
                imageUrls.add(imageUri);
            }

            updateGalleryRecyclerView();
        }
    }

    private void updateGalleryRecyclerView() {
        RecyclerView galleryRecyclerView = findViewById(R.id.gallery_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        galleryRecyclerView.setLayoutManager(layoutManager);
        GalleryAdapter galleryAdapter = new GalleryAdapter(this, imageUrls);
        galleryRecyclerView.setAdapter(galleryAdapter);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            // get the image from the device
//            selectedImageUri = data.getData();
//            // add of the image url in the list
//            imageUrls.add(selectedImageUri);
//
//            ListView galleryView = findViewById(R.id.gallery_posting);
//
//            // pass the image uri into the adapter and
//            // then use that adapter to bring the image into the listview
//            GalleryAdapter galleryAdapter = new GalleryAdapter(this, imageUrls);
//            galleryView.setAdapter(galleryAdapter);
//        }
//    }


//    old code for getting image from the device and put to the app
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            Uri imageUri = data.getData();
//            ImageView photoView = findViewById(R.id.photoView);
//            photoView.setImageURI(imageUri);
//        }
//    }

    private void post_toFeed() {
        // Initialize your variables here
        List<String> photoUrls = new ArrayList<>();
        for (Uri uri : imageUrls) {
            photoUrls.add(uri.toString());
        }
        String video = ""; // Initialize video
        String caption = CaptionField.getText().toString().trim();
        String dateTime = ""; // Initialize dateTime

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
                    Posts post = new Posts(nickname, avatar, photoUrls, video, caption, dateTime, comments, reaction);

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

}