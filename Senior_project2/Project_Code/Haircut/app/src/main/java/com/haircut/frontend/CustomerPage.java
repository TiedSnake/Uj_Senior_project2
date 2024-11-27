package com.haircut.frontend;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.haircut.R;

public class CustomerPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            // Default fragment when the app first opens (Home)
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_home_customer()).commit();
            navigationView.setCheckedItem(R.id.sidebar_option_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.sidebar_option_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_home_customer()).commit();
        } else if (item.getItemId() == R.id.sidebar_option_about_us) { // About Us
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_about_us()).commit();
        } else if (item.getItemId() == R.id.sidebar_option_profile) { // Profile
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UpdateProfile()).commit();
        } else if (item.getItemId() == R.id.sidebar_option_appointment) { // Book Appointment
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_barbershop_list()).commit();
        } else if (item.getItemId() == R.id.sidebar_option_chat) { // Chat with Barber
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_chat_with_barber()).commit();
        } else if (item.getItemId() == R.id.sidebar_option_rate) { //Rate barbershop
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_rate_barbershop()).commit();
        } else if (item.getItemId() == R.id.sidebar_option_signout) { // Sign Out
            new FragmentSignOutDialog().show(getSupportFragmentManager(), "SignOutDialog");

//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentSignOutDialog()).commit();
        }

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
