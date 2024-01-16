package com.example.meetmi;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetmi.customAdapter.FrameAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import ModelClass.Posts;
import ModelClass.UserCallback;
import ModelClass.UserManager;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
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
}


