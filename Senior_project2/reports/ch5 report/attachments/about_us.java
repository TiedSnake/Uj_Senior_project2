package com.example.haircut;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class about_us extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us_page);  // Use your XML layout here

        // Find the ImageButton (back button) by its ID
        ImageButton backButton = findViewById(R.id.backButton);

        // Set a click listener on the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate back to the customer activity
                Intent intent = new Intent(about_us.this, customer.class);
                startActivity(intent);
                finish();  // Optional: close the current activity
            }
        });
    }
}
