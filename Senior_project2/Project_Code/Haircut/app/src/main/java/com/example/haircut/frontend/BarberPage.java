package com.example.haircut.frontend;

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

import com.example.haircut.R;
import com.google.android.material.navigation.NavigationView;

public class BarberPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
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
            toolbar.setNavigationIcon(R.drawable.ic_menu); // Ensure you have the right icon resource
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
        if (item.getItemId() == R.id.btnHome) {
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new Home()).commit();
        } else if (item.getItemId() == R.id.btnProfile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new profile1()).commit();
        } else if (item.getItemId() == R.id.btnMenu) {
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new menu()).commit();
        } else if (item.getItemId() == R.id.btnAppointments) {
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new fragment_appointment()).commit();
        } else if (item.getItemId() == R.id.btnReviews) {
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new fragment_reviews()).commit();
        } else if (item.getItemId() == R.id.btnSignOut) {
            // Handle sign-out: clear session, redirect to WelcomePage
            signOut();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOut() {
        // Clear session data if necessary (e.g., shared preferences or any auth token)
        // Then, navigate to the WelcomePage activity
        Intent intent = new Intent(BarberPage.this, WelcomePage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
        startActivity(intent);
        Toast.makeText(this, "Signed out successfully!", Toast.LENGTH_SHORT).show();
        finish(); // Close current activity
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
