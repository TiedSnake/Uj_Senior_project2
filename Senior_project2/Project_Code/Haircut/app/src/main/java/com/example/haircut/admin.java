package com.example.haircut;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class admin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_page);  // Replace with your actual layout name if needed

        // Find buttons by their IDs
        Button btnViewRatings = findViewById(R.id.btnViewRatings);
        Button btnSignOut = findViewById(R.id.btnSignOut);

        // Set click listener for the View Ratings/Reviews button
        btnViewRatings.setOnClickListener(v -> {
            // Code to handle "View Ratings/Reviews" button click
            // You can start a new activity or show the reviews here
            Intent intent = new Intent(admin.this, view_ratings.class); // Example activity
            startActivity(intent);
        });

        // Set click listener for the Sign Out button
        btnSignOut.setOnClickListener(v -> {
            // Code to handle "Sign Out" button click
            // For example, log the user out and redirect them to the login page
            Intent intent = new Intent(admin.this, login.class); // Example activity
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear the activity stack
            startActivity(intent);
            finish(); // Close the current activity
        });
    }
}

