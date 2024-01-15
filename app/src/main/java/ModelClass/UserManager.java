package ModelClass;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.meetmi.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

public class UserManager {

    // Method to get the current Firebase user
    public static FirebaseUser getCurrentUser() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        return mAuth.getCurrentUser();
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
