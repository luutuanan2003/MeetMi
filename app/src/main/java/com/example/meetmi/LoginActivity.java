package com.example.meetmi;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;

    private Button login, signupButton;
    private SignInButton googleSignInButton;
    private EditText usernameField, passwordField;
    private TextView errorText;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setStatusBarColor(Color.parseColor("#1F1F1F"));

        // Initialize UI components
        login = findViewById(R.id.login);
        signupButton = findViewById(R.id.signupButtonLogin);
        googleSignInButton = findViewById(R.id.google_sign_in_button);
        usernameField = findViewById(R.id.usernameLogin);
        passwordField = findViewById(R.id.passwordLogin);
        errorText = findViewById(R.id.errorTextLogin);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Set OnClickListener for Google Sign-In button
        googleSignInButton.setOnClickListener(v -> googleSignIn());

        // Set OnClickListener for login button
        login.setOnClickListener(v -> {
            String enteredUsername = usernameField.getText().toString();
            String enteredPassword = passwordField.getText().toString();
            authenticateUser(enteredUsername, enteredPassword);
        });

        // Set OnClickListener for signup button
        signupButton.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignupActivity.class)));
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
                            errorText.setText("Wrong username or password.");
                            setVisible(R.id.errorTextLogin, true);
                        }
                    }
                } else {
                    errorText.setText("Wrong username or password.");
                    setVisible(R.id.errorTextLogin, true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                errorText.setText("An error occurred. Please try again.");
                setVisible(R.id.errorTextLogin, true);
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
                        errorText.setText("Authentication failed. Please try again.");
                        errorText.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void setVisible(int id, boolean isVisible) {
        View aView = findViewById(id);
        aView.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign-In failed, update UI appropriately
                errorText.setText("Google Sign-In failed.");
                errorText.setVisibility(View.VISIBLE);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Intent intent = new Intent(LoginActivity.this, FeedActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // If sign in fails, display a message to the user.
                        errorText.setText("Authentication failed.");
                        errorText.setVisibility(View.VISIBLE);
                    }
                });
    }
}

