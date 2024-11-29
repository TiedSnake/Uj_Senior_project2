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
        //        super.onCreate(savedInstanceState);
//        // FIXME: 11/28/24 Needs to point to the admin siderbar like customer & barber pages.
//        setContentView(R.layout.admin_page);
//
//        toolbar = findViewById(R.id.admin_toolbar);
//        setSupportActionBar(toolbar);
//
//        // Set the navigation icon for the Toolbar
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            toolbar.setNavigationIcon(R.drawable.ic_menu); // Ensure you have the right icon resource
//        }
//
//        drawerLayout = findViewById(R.id.admin_drawer_layout);
//        navigationView = findViewById(R.id.admin_nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//
//        // Set up the ActionBarDrawerToggle
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
//        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.gray)); // Set icon color
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState(); // Sync the toggle state with the drawer
//
//        // Load default fragment
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new BarberHome()).commit();
//            navigationView.setCheckedItem(R.id.btnHome);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home_admin);

        // Find views by their IDs
        Button btnViewRatings = findViewById(R.id.btnViewRatings);
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
            Intent intent = new Intent(AdminPage.this, fragment_view_ratings.class); // Example activity
            startActivity(intent);
        });
    }
}
