package com.example.meetmi;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetmi.customAdapter.FrameAdapter;

public class ProfileActivity extends AppCompatActivity {
    Button changePassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getWindow().setStatusBarColor(Color.parseColor("#1F1F1F"));

        setVisible(R.id.set_password_profile,false);

        changePassword = (Button) findViewById(R.id.password_profile);

        RecyclerView frameRecyclerView = findViewById(R.id.frame_recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        frameRecyclerView.setLayoutManager(layoutManager);

        FrameAdapter frameAdapter = new FrameAdapter(this); // Pass context here
        frameRecyclerView.setAdapter(frameAdapter);

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


}


