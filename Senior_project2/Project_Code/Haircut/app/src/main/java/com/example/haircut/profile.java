package com.example.haircut;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);

        // Get the TextViews
        TextView firstNameTextView = findViewById(R.id.first_name);
        TextView lastNameTextView = findViewById(R.id.last_name);
        TextView emailTextView = findViewById(R.id.email);

        // Retrieve user data (for example, from intent or shared preferences)
        String firstName = getIntent().getStringExtra("FIRST_NAME");
        String lastName = getIntent().getStringExtra("LAST_NAME");
        String email = getIntent().getStringExtra("EMAIL");

        // Set user data to TextViews
        firstNameTextView.setText(firstName);
        lastNameTextView.setText(lastName);
        emailTextView.setText(email);

        // Handle the Go Back button
        Button goBackButton = findViewById(R.id.go_back_button);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Finish the current activity to go back to the previous one
                finish();
            }
        });
    }
}
