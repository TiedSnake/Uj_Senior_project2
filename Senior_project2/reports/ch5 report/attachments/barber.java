package com.example.haircut;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

public class barber extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barber_page);

        // Initialize views
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set the navigation icon for the Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_menu);
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Set up the ActionBarDrawerToggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.gray)); // Set icon color
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState(); // Sync the toggle state with the drawer

        // Load default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new Home()).commit();
            navigationView.setCheckedItem(R.id.btnHome);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        String title = "Home"; // Default title

        if (item.getItemId() == R.id.btnHome) {
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new Home()).commit();
            title = "Home"; //Title for Home page (header)
        } else if (item.getItemId() == R.id.btnProfile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new profile1()).commit();
            title = "Profile"; //Title for profile page (header)
        } else if (item.getItemId() == R.id.btnMenu) {
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new menu()).commit();
            title = "Menu"; //Title for services Menu page (header)
        } else if (item.getItemId() == R.id.btnEditMenu) {
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new fragment_edit_service_menu()).commit();
            title = "Edit Menu"; //Title for edit services Menu page (header)
        } else if (item.getItemId() == R.id.btnAppointments) {
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new fragment_appointment()).commit();
            title = "Appointments"; //Title for Appointments page (header)
        } else if (item.getItemId() == R.id.btnReviews) {
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new fragment_reviews()).commit();
            title = "Reviews"; //Title for Reviews page (header)
        } else if (item.getItemId() == R.id.btnSignOut) {
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new fragment_sign_out()).commit();
            title = "Sign Out"; //Title for Sign out page (header)
        }


        // Update toolbar title based on selected item
        toolbar.setTitle(title);

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
