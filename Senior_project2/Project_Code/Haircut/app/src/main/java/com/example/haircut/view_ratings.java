package com.example.haircut;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class view_ratings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_ratings_page);

        // Go Back Button
        ImageButton btnGoBack = findViewById(R.id.btnGoBack);
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle back button press
//                finish(); // Finish the current activity and go back to the previous one
            }
        });
    }
}
