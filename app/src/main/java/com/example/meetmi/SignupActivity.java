package com.example.meetmi;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meetmi.Users;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SignupActivity extends AppCompatActivity {

    private EditText usernameField, passwordField;
    // Add other relevant fields if needed
    private Button signupButton;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize fields
        usernameField = findViewById(R.id.usernameField);
        passwordField = findViewById(R.id.passwordField);
        signupButton = findViewById(R.id.signupButton);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        // Validation here for input values

        // Initialize other fields with default or empty values
        String avatar = ""; // default or empty
        String id = ""; // generate or leave empty
        double latitude = 0; // default value
        double longitude = 0; // default value
        List<String> friends = new ArrayList<>(); // empty list
        String photoFrameId = ""; // default or empty
        List<String> newsfeed = new ArrayList<>(); // empty list
        String nickname = ""; // default or empty

        // Creating user object
        Users user = new Users(username, password, avatar, id, latitude, longitude,
                friends, photoFrameId, newsfeed, nickname);

        // Saving to Firebase
        mDatabase.child("users").push().setValue(user);
    }
}
