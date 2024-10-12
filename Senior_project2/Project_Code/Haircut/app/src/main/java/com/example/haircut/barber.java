package com.example.haircut;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class barber extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barber_page);

        // Find buttons by their IDs
        Button btnProfile = findViewById(R.id.btnProfile);
        Button btnEditService = findViewById(R.id.btnEditService);
        Button btnAppointments = findViewById(R.id.btnAppointments);
        Button btnContactBarber = findViewById(R.id.btnContactBarber);
        Button btnPayment = findViewById(R.id.btnPayment);
        Button btnCheckReviews = findViewById(R.id.btnCheckReviews);
        Button btnSignOut = findViewById(R.id.btnSignOut);

        // Set click listeners for each button
        btnProfile.setOnClickListener(view -> {
            // Handle Profile click
        });

        btnEditService.setOnClickListener(view -> {
            // Handle Edit Service click
        });

        btnAppointments.setOnClickListener(view -> {
            // Handle Appointments click
        });

        btnContactBarber.setOnClickListener(view -> {
            // Handle Contact Barber click
        });

        btnPayment.setOnClickListener(view -> {
            // Handle Payment click
        });

        btnCheckReviews.setOnClickListener(view -> {
            // Handle Check Reviews click
        });

        btnSignOut.setOnClickListener(view -> {
            // Handle Sign Out click
        });
    }
}
