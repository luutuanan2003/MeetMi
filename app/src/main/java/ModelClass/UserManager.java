package ModelClass;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.meetmi.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

public class UserManager {

    // Method to get the current Firebase user
    public static FirebaseUser getCurrentUser() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        return mAuth.getCurrentUser();
    }

    public interface UserCallback {
        void onCallback(Users user);
    }

    // Callback interface for handling the retrieved posts
    public interface PostsCallback {
        void onPostsReceived(List<Posts> posts);
        void onError(String error);
    }

    // Method in the UserManager class to retrieve posts
    public static void getUserPosts(PostsCallback postsCallback) {
        getCurrentUserDetail(new UserCallback() {
            @Override
            public void onCallback(Users user) {
                if (user != null) {
                    // If user is found, use the email to retrieve posts
                    DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("posts");
                    postsRef.orderByChild("user_Email").equalTo(user.getEmail())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        List<Posts> userPosts = new ArrayList<>();
                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                            // Deserialize each child into a Posts object
                                            try {
                                                Posts post = postSnapshot.getValue(Posts.class);
                                                if (post != null) {
                                                    post.setKeyID(postSnapshot.getKey());
                                                    userPosts.add(post);
                                                }
                                            } catch (DatabaseException e) {
                                                Log.e("FirebaseCheck", "Error deserializing post", e);
                                            }
                                        }
                                        postsCallback.onPostsReceived(userPosts);
                                    } else {
                                        Log.d("FirebaseCheck", "No posts found for the user");
                                        postsCallback.onError("No posts found for the user");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.e("FirebaseCheck", "Error while reading posts", databaseError.toException());
                                    postsCallback.onError("Error while reading posts");
                                }
                            });
                } else {
                    Log.d("FirebaseCheck", "User not found");
                    postsCallback.onError("User not found");
                }
            }
        });
    }


    // Method to get user's details such as email or UID
    public static void getCurrentUserDetail(UserCallback userCallback) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            String email = firebaseUser.getEmail();

            usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Users user = snapshot.getValue(Users.class);

                            userCallback.onCallback(user);
                            break; // assuming email is unique and you want the first match
                        }
                    }
                    else {
                        Log.d("FirebaseCheck", "No data found for the user");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }

            });
        }
    }



}
