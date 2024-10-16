package com.example.haircut;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class barber extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barber_page);
        // Initialize DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.drawer_Layout);
        navigationView = findViewById(R.id.nav_view);

        // Set up ActionBarDrawerToggle to link the DrawerLayout with the action bar
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Set NavigationItemSelectedListener for menu options
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.btnHome)
                    Toast.makeText(barber.this, "Home Selected", Toast.LENGTH_SHORT).show();
                else if (item.getItemId() == R.id.btnProfile)
                    Toast.makeText(barber.this, "Profile Selected", Toast.LENGTH_SHORT).show();
                else if (item.getItemId() == R.id.btnMenu)
                    Toast.makeText(barber.this, "Services Menu Selected", Toast.LENGTH_SHORT).show();
                else if (item.getItemId() == R.id.btnAppointments)
                    Toast.makeText(barber.this, "Appointments Selected", Toast.LENGTH_SHORT).show();
                else if (item.getItemId() == R.id.btnCheckReviews)
                    Toast.makeText(barber.this, "Reviews Selected", Toast.LENGTH_SHORT).show();
                else if (item.getItemId() == R.id.btnSignOut)
                    Toast.makeText(barber.this, "Sign out Selected", Toast.LENGTH_SHORT).show();

                // Close the drawer after selecting an item
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
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
