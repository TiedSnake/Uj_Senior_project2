package com.example.haircut;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class customer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_page);  // Set to the customer_page layout

        // Find the ImageButton by ID
        ImageButton menuButton = findViewById(R.id.menuImageButton);
        // Find the About Us button by ID
        Button aboutUsButton = findViewById(R.id.sidebarOption1); // Make sure to use the correct ID here
        // Find the Sign Out button by ID
        Button btnSignOut = findViewById(R.id.sidebarOption5); // Assuming this is the ID for the Sign Out button

        // Set an onClick listener for the menu button
        menuButton.setOnClickListener(v -> toggleSidebarVisibility());

        // Set an onClick listener for the About Us button
        aboutUsButton.setOnClickListener(v -> {
            // Create an Intent to navigate to the about_us activity
            Intent intent = new Intent(customer.this, about_us.class);
            startActivity(intent);
        });

        // Set an onClick listener for the Sign Out button
        btnSignOut.setOnClickListener(v -> {
            // Code to handle "Sign Out" button click
            Intent intent = new Intent(customer.this, login.class); // Redirect to the login activity
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear the activity stack
            startActivity(intent);
            finish(); // Optional: close the current activity
        });
    }

    private void toggleSidebarVisibility() {
        // Find the sidebar layout by ID
        ConstraintLayout sidebar = findViewById(R.id.sidebarMenu);

        // Toggle visibility between VISIBLE and INVISIBLE
        if (sidebar.getVisibility() == View.VISIBLE) {
            sidebar.setVisibility(View.INVISIBLE);
        } else {
            sidebar.setVisibility(View.VISIBLE);
        }
    }
}
