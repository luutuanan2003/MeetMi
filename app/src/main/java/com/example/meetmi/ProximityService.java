package com.example.meetmi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProximityService extends Service {

    private static final String TAG = "ProximityService";
    private DatabaseReference databaseReference;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Street Pass", "Service Created");

        databaseReference = FirebaseDatabase.getInstance().getReference();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateFirebaseData(location);
                checkForNearbyUsers(location);
            }


        };

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, locationListener);
        } catch (SecurityException e) {
            Log.e(TAG, "Location permission not granted", e);
        }
    }

    private void updateFirebaseData(Location location) {
        String nickname = "nickname"; // Replace with actual user ID
        databaseReference.child("users").child(nickname).child("latitude").setValue(location.getLatitude());
        databaseReference.child("users").child(nickname).child("longitude").setValue(location.getLongitude());
    }

    private void checkForNearbyUsers(Location currentLocation) {
        final double PROXIMITY_RADIUS = 20; // meters
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    double userLat = userSnapshot.child("latitude").getValue(Double.class);
                    double userLon = userSnapshot.child("longitude").getValue(Double.class);
                    Location userLocation = new Location("");
                    userLocation.setLatitude(userLat);
                    userLocation.setLongitude(userLon);

                    float distance = currentLocation.distanceTo(userLocation);
                    if (distance < PROXIMITY_RADIUS) {
                        Log.d("StressPass", "Nearby user found: " + userSnapshot.getKey());
                        triggerNotification(userSnapshot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Database error: " + databaseError.getMessage());
            }
        });
    }

    private void triggerNotification(String userId) {
        // Sample Notification Code
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "proximity_notification_channel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Proximity Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle("Nearby User Detected")
                .setContentText("User " + userId + " is nearby. Tap to connect.")
                .setSmallIcon(R.drawable.logo) // Replace with your app's icon
                .build();

        notificationManager.notify(1, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service Started");
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

