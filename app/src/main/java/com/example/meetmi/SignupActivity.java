package com.example.meetmi;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SignupActivity extends AppCompatActivity {

    private EditText usernameField, passwordField, emailField;
    // Add other relevant fields if needed
    private Button signupButton;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getWindow().setStatusBarColor(Color.parseColor("#1F1F1F"));

        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize fields
        usernameField = findViewById(R.id.usernameField);
        passwordField = findViewById(R.id.passwordField);
        signupButton = findViewById(R.id.signupButtonLogin);
        emailField = findViewById(R.id.user_emailField);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String email = emailField.getText().toString().trim();
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
        Users user = new Users(email,username, password, avatar, id, latitude, longitude,
                friends, photoFrameId, newsfeed, nickname);

        // Saving to Firebase
        mDatabase.child("users").push().setValue(user);
        showConfirmationDialog();
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Account has been created successfully.")
                .setCancelable(false)
                .setPositiveButton("Back to Sign In", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Start the Sign In activity
                        Intent signInIntent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(signInIntent);
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
