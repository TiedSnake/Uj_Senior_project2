package com.haircut.frontend;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.haircut.R;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        // Get the TextViews
        TextView firstNameTextView = findViewById(R.id.f_name);
        TextView lastNameTextView = findViewById(R.id.l_name);
        TextView emailTextView = findViewById(R.id.email_field);

        // Retrieve user data (for example, from intent or shared preferences)
        String firstName = getIntent().getStringExtra("FIRST_NAME");
        String lastName = getIntent().getStringExtra("LAST_NAME");
        String email = getIntent().getStringExtra("EMAIL");

        // Set user data to TextViews
        firstNameTextView.setText(firstName);
        lastNameTextView.setText(lastName);
        emailTextView.setText(email);
// Find the ImageButton (back button) by its ID
        ImageButton backButton = findViewById(R.id.backButton);

        // Set a click listener on the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate back to the customer activity
                Intent intent = new Intent(Profile.this, CustomerSidebar.class);
                startActivity(intent);
                finish();  // Optional: close the current activity
            }
        });
        // Handle the Go Back button
        Button update_profile = findViewById(R.id.update_profile);
        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Finish the current activity to go back to the previous one
                finish();
            }
        });
    }
}
