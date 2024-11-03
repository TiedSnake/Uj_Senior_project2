package com.example.haircut;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.google.android.material.navigation.NavigationView;

public class admin extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_page);  // Make sure to reference your XML layout

        // Set up the toolbar and make it the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);  // Make sure this ID matches the one in admin_page.xml
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Set up the toggle for the navigation drawer with the toolbar
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Set the initial fragment to display (optional)
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new fragment_home_admin()).commit();
            navigationView.setCheckedItem(R.id.btnHome);  // Make sure this ID exists in your menu XML
        }

        // Set up navigation item selection listener
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                // Use if-else statements for navigation
                if (item.getItemId() == R.id.btnHome) {
                    selectedFragment = new fragment_home_admin();
                } else if (item.getItemId() == R.id.btnProfile) {
                    selectedFragment = new profile1();
                } else if (item.getItemId() == R.id.btnSignOut) {
                    selectedFragment = new fragment_sign_out();
                }

                // Replace the current fragment with the selected one
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, selectedFragment).commit();
                }

                // Close the drawer after item selection
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
