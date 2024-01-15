package com.example.meetmi;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    Button login;
    Button signupButton;
    EditText usernameField; // Add EditText for username
    EditText passwordField; // Add EditText for password
    TextView errorText; // Add TextView for error messages
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setStatusBarColor(Color.parseColor("#1F1F1F"));

        login = (Button) findViewById(R.id.login);
        signupButton = (Button) findViewById(R.id.signupButtonLogin);
        usernameField = (EditText) findViewById(R.id.usernameLogin); // Initialize EditText for username
        passwordField = (EditText) findViewById(R.id.passwordLogin); // Initialize EditText for password
        errorText = (TextView) findViewById(R.id.errorTextLogin); // Initialize TextView for error messages
        mAuth = FirebaseAuth.getInstance();

        // set default setting for the login screen
        setVisible(R.id.errorTextLogin, false);


        // this login feature is only to take to the newsfeed immediately with no
        // authentication


            login.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String enteredUsername = usernameField.getText().toString();
             String enteredPassword = passwordField.getText().toString();
              // Perform authentication
              authenticateUser(enteredUsername, enteredPassword);
       }
   });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void authenticateUser(String username, String password) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        usersRef.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        Users user = userSnapshot.getValue(Users.class);
                        if (user.getPassword().equals(password)) {
                            // authenticate use email and password
                            signInWithFirebase(user.getEmail(),password);
                        } else {
                            // Passwords do not match, show error
                            setVisible(R.id.errorTextLogin, true);
                        }
                    }
                } else {
                    // Username not found, show error
                    setVisible(R.id.errorTextLogin, true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }

    private void signInWithFirebase(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, proceed to next activity
                        Intent intent = new Intent(LoginActivity.this, FeedActivity.class);
                        startActivity(intent);
                        finish(); // Close the LoginActivity
                    } else {
                        // If Firebase sign in fails, handle the failure
                        // TODO: have an error message when firebase not connected
                        errorText.setVisibility(View.VISIBLE);
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
}

