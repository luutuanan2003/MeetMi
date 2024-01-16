package com.example.meetmi;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetmi.customAdapter.FrameAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import ModelClass.Posts;
import ModelClass.UserCallback;
import ModelClass.UserManager;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 123;
    Button changePassword;
    private DatabaseReference mDatabase;
    private TextView nickName_profile;
    private ImageView change_userAvatar, change_NickName;
    private CircleImageView userAvatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getWindow().setStatusBarColor(Color.parseColor("#1F1F1F"));

        setVisible(R.id.set_password_profile,false);

        change_userAvatar = findViewById(R.id.changeUserAvatarP);
        change_NickName = findViewById(R.id.changeUserNicknameP);
        nickName_profile = findViewById(R.id.nickname_profile);
        userAvatar = findViewById(R.id.userAvatar_Profile);
        changePassword = (Button) findViewById(R.id.password_profile);

        //change nickname
        change_NickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeNicknameDialog();
            }
        });

        //change avatar
        change_userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        //set user nickName + avatar
        mDatabase = FirebaseDatabase.getInstance().getReference();
        UserManager.getCurrentUserDetail(new UserCallback() {
            @Override
            public void onCallback(Users user) {
                if (user != null) {
                    // Now that we have the user, we can create and post
                    String nickname = user.getNickname();
                    String avatar = user.getAvatar().toString(); // Assuming getAvatar() method exists
                    nickName_profile.setText(nickname);
                    // Check if the avatar URL is not null and not empty
                    if (avatar != null && !avatar.isEmpty()) {
                        Picasso.get()
                                .load(avatar)
                                .into(userAvatar);
                    } else {
                        // Load a default image if the avatar URL is null or empty
                        Picasso.get()
                                .load(R.drawable.sampleavatar) // Replace with your default avatar drawable
                                .into(userAvatar);
                    }


                } else {
                    showUserNotFoundDialog();

                }
            }
        });


        RecyclerView frameRecyclerView = findViewById(R.id.frame_recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        frameRecyclerView.setLayoutManager(layoutManager);

        FrameAdapter frameAdapter = new FrameAdapter(this); // Pass context here
        frameRecyclerView.setAdapter(frameAdapter);


        // change password method
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View cardview = findViewById(R.id.set_password_profile); // Replace with your layout's ID
                if (cardview.getVisibility() == View.VISIBLE) {
                    setVisible(R.id.set_password_profile,false);
                } else {
                    setVisible(R.id.set_password_profile,true);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            String imageUrl = imageUri.toString();
            updateAvatar(imageUrl);
        }
    }

    private void updateAvatar(String newAvatarUrl) {
        FirebaseUser firebaseUser = UserManager.getCurrentUser();
        if (firebaseUser != null) {
            String email = firebaseUser.getEmail();
            String encodedEmail = encodeUserEmail(email); // Encode the email
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

            // Query the database to find the user node that matches the email address
            usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            // Update the avatar at this node
                            childSnapshot.getRef().child("avatar").setValue(newAvatarUrl).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // Update the ImageView with the new avatar
                                    Picasso.get().load(newAvatarUrl).into(userAvatar);
                                    Log.d("UpdateAvatar", "Avatar updated successfully for email: " + email);
                                } else {
                                    // Handle the error, e.g., show a Toast or AlertDialog
                                    Log.e("UpdateAvatar", "Failed to update avatar", task.getException());
                                    showUpdateAvatarFail();
                                }
                            });
                            break; // Assuming the email is unique, break after finding the first match
                        }
                    } else {
                        Log.e("UpdateAvatar", "No user found with email: " + email);
                        showUpdateAvatarFail();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("UpdateAvatar", "Database error while updating avatar", databaseError.toException());
                    showUpdateAvatarFail();
                }
            });
        } else {
            Log.e("UpdateAvatar", "FirebaseUser is null");
            showUpdateAvatarFail();
        }
    }

    private void showUpdateAvatarFail() {
       Log.d("Avatar Failed",  "Failed");
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void showChangeNicknameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Change Your NickName");

        // Set up the input
        final EditText input = new EditText(ProfileActivity.this);
        // Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newNickname = input.getText().toString().trim();
                if (!newNickname.isEmpty()) {
                    updateNickname(newNickname);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void updateNickname(String newNickname) {
        FirebaseUser firebaseUser = UserManager.getCurrentUser();
        if (firebaseUser != null) {
            String email = firebaseUser.getEmail();
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

            // Query the database to find the user node that matches the email address
            usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            // Update the nickname at this node
                            childSnapshot.getRef().child("nickname").setValue(newNickname).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // Update the TextView with the new nickname
                                    nickName_profile.setText(newNickname);
                                    Log.d("UpdateNickname", "Nickname updated successfully for email: " + email);
                                } else {
                                    // Handle the error, e.g., show a Toast or AlertDialog
                                    Log.e("UpdateNickname", "Failed to update nickname", task.getException());
                                    showUpdateNickNameFail();
                                }
                            });
                            break; // Assuming the email is unique, break after finding the first match
                        }
                    } else {
                        Log.e("UpdateNickname", "No user found with email: " + email);
                        showUpdateNickNameFail();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("UpdateNickname", "Database error while updating nickname", databaseError.toException());
                    showUpdateNickNameFail();
                }
            });
        } else {
            Log.e("UpdateNickname", "FirebaseUser is null");
            showUpdateNickNameFail();
        }
    }



    public static String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

    public static String decodeUserEmail(String userKey) {
        return userKey.replace(",", ".");
    }

    private void setVisible(int id, boolean isVisible){
        View aView = findViewById(id);
        if (isVisible)
            aView.setVisibility(View.VISIBLE);
        else
            aView.setVisibility(View.INVISIBLE);
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

    private void showUpdateNickNameFail() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("UpdateFail");
        builder.setMessage("Update NickName Failed");
        builder.setPositiveButton("OK", (dialog, id) -> {
            dialog.dismiss();
        });
        builder.setNegativeButton("Cancel", (dialog, id) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}


