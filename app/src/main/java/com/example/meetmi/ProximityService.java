package com.example.meetmi;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import ModelClass.UserManager;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import ModelClass.UserCallback;

import com.example.meetmi.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import ModelClass.UserManager;

public class ProximityService extends Service {

    private static final String TAG = "ProximityService";
    private DatabaseReference databaseReference;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private String currentUserEmail;



    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Street Pass", "Service Created");

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        UserManager.getCurrentUserDetail(new UserManager.UserCallback() {
            @Override
            public void onCallback(Users user) {
                if (user != null) {
                    currentUserEmail = user.getEmail();

                }
            }
        });

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                checkForNearbyUsers(location);
                updateFirebaseData(location);

            }


        };

        if (checkLocationPermissions()) {
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, locationListener);
            } catch (SecurityException e) {
                Log.e("Street Pass", "Location permission not granted", e);
            }
        } else {
            Log.e("Street Pass", "Location permissions not granted.");
        }
    }

    private boolean checkLocationPermissions() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void updateFirebaseData(Location location) {
        if (currentUserEmail == null || location == null) {
            Log.e(TAG, "Email or location is null. Cannot update Firebase data.");
            return;
        }

        // Query the users by email to find the current user's unique key
        databaseReference.orderByChild("email").equalTo(currentUserEmail)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                            // Assuming email is unique and can only have one entry, get the unique key
                            DataSnapshot userSnapshot = dataSnapshot.getChildren().iterator().next();
                            String currentUserKey = userSnapshot.getKey();

                            // Now we have the unique key, update the user's location
                            DatabaseReference currentUserRef = databaseReference.child(currentUserKey);

                            Map<String, Object> locationUpdateMap = new HashMap<>();
                            locationUpdateMap.put("latitude", location.getLatitude());
                            locationUpdateMap.put("longitude", location.getLongitude());

                            currentUserRef.updateChildren(locationUpdateMap)
                                    .addOnSuccessListener(aVoid -> Log.d(TAG, "User location updated successfully."))
                                    .addOnFailureListener(e -> Log.e(TAG, "Failed to update user location", e));
                        } else {
                            // Handle the case where the current user is not found
                            Log.e(TAG, "Current user not found in the database.");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle database error
                        Log.e(TAG, "Database error: " + databaseError.getMessage());
                    }


                });

    }


    private void checkForNearbyUsers(Location currentLocation) {
        final double PROXIMITY_RADIUS = 200; // meters
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Users user = userSnapshot.getValue(Users.class);
                    if (user != null && currentUserEmail != null && user.getEmail() != null && user.getEmail().equals(currentUserEmail)) {
                        continue; // Skip the current user
                    }

                    // Check if latitude and longitude are available
                    if (userSnapshot.child("latitude").exists() && userSnapshot.child("longitude").exists()) {
                        Double userLat = userSnapshot.child("latitude").getValue(Double.class);
                        Double userLon = userSnapshot.child("longitude").getValue(Double.class);

                        if (userLat == null || userLon == null) {
                            // Skip if latitude or longitude is null
                            continue;
                        }

                        // Create a Location object for the other user's location
                        Location userLocation = new Location("");
                        userLocation.setLatitude(userLat);
                        userLocation.setLongitude(userLon);

                        // Calculate the distance and check if the other user is nearby
                        float distance = currentLocation.distanceTo(userLocation);
                        if (distance < PROXIMITY_RADIUS) {
                            // Log and notify about the nearby user
                            String nicknames = user.getNickname();
                            Log.d("Steetpass", "Nearby user found: " + userSnapshot.getKey() + " : "  + nicknames);
                            triggerNotification(userSnapshot.getKey());

                            // Show Toast message with the user's nickname
                            String nickname = user.getNickname();
                            String message = "Nearby user found: " + nickname;
                            new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show());
                        }
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Database error: " + databaseError.getMessage());
            }
        });
    }






    private void triggerNotification(String userId) {
        // Implement your notification logic here
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Street Pass", "Service Started");
        Toast.makeText(this, "Proximity Service Started", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service Destroyed");
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
}