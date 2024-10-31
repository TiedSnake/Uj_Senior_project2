package com.example.haircut;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import android.view.MenuItem;

public class admin extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_page); //getting layout needed

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // Set up the toolbar

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this); // Set the listener

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open, R.string.close); // Toggle for opening and closing the drawer
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            // Default fragment when the app first opens (Home)
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new fragment_home_admin()).commit();
            navigationView.setCheckedItem(R.id.btnHome);
            toolbar.setTitle(R.string.sidebar_headerText); // Set initial title to Home
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation item selection
        if (item.getItemId() == R.id.btnHome) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new fragment_home_admin()).commit();
            toolbar.setTitle(R.string.sidebar_headerText); // Title for Home page (Header)
        } else if (item.getItemId() == R.id.btnProfile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new profile1()).commit();
            toolbar.setTitle(R.string.profile); // Title for Profile page (Header)
        } else if (item.getItemId() == R.id.btnSignOut) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new fragment_sign_out()).commit();
            toolbar.setTitle(R.string.sign_out_barber); // Title for Sign Out page (Header)
        }

        drawerLayout.closeDrawer(GravityCompat.START); // Close the drawer after selection
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

