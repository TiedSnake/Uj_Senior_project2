package com.haircut.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.haircut.R;

import java.util.Arrays;
import java.util.List;

public class AdminPage extends AppCompatActivity {

    private RecyclerView rvBarbers;
    private RecyclerView rvClients;
    private boolean isBarbersVisible = false;  // Track visibility of Barber list
    private boolean isClientsVisible = false;  // Track visibility of Client list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_page);  // Make sure this is your actual layout

        // Find views by their IDs
        Button btnViewRatings = findViewById(R.id.btnViewRatings);
        Button btnSignOut = findViewById(R.id.btnSignOut);
        Button tabBarber = findViewById(R.id.tabBarber);
        Button tabClients = findViewById(R.id.tabClients);
        rvBarbers = findViewById(R.id.rvBarbers);
        rvClients = findViewById(R.id.rvClients);

        // Barber and client lists
        List<String> barberList = Arrays.asList("Barber 1", "Barber 2", "Barber 3");
        List<String> clientList = Arrays.asList("Client 1", "Client 2", "Client 3");

        // Set up adapters
        BarberAdapter barberAdapter = new BarberAdapter(barberList);
        ClientAdapter clientAdapter = new ClientAdapter(clientList);

        rvBarbers.setAdapter(barberAdapter);
        rvBarbers.setLayoutManager(new LinearLayoutManager(this));

        rvClients.setAdapter(clientAdapter);
        rvClients.setLayoutManager(new LinearLayoutManager(this));

        // Initially hide the RecyclerViews
        rvBarbers.setVisibility(View.GONE);
        rvClients.setVisibility(View.GONE);

        // Barber button click listener
        tabBarber.setOnClickListener(v -> {
            // Toggle barber list visibility
            isBarbersVisible = !isBarbersVisible;
            rvBarbers.setVisibility(isBarbersVisible ? View.VISIBLE : View.GONE);

            // Hide client list if visible
            if (isClientsVisible) {
                rvClients.setVisibility(View.GONE);
                isClientsVisible = false;
            }
        });

        // Client button click listener
        tabClients.setOnClickListener(v -> {
            // Toggle client list visibility
            isClientsVisible = !isClientsVisible;
            rvClients.setVisibility(isClientsVisible ? View.VISIBLE : View.GONE);

            // Hide barber list if visible
            if (isBarbersVisible) {
                rvBarbers.setVisibility(View.GONE);
                isBarbersVisible = false;
            }
        });

        // View Ratings/Reviews button click listener
        btnViewRatings.setOnClickListener(v -> {
            Intent intent = new Intent(AdminPage.this, ViewRatingsPage.class); // Example activity
            startActivity(intent);
        });

        // Sign Out button click listener
        btnSignOut.setOnClickListener(v -> {
            Intent intent = new Intent(AdminPage.this, WelcomePage.class); // Example activity
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear activity stack
            startActivity(intent);
            finish(); // Close current activity
        });
    }
}
