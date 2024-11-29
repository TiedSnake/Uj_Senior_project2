package com.haircut.frontend;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.haircut.R;
import com.haircut.backend.Service;
import com.haircut.backend.User;

public class CustomerPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_page);
        User user = Service.getCurrentUser();
        Toolbar toolbar = findViewById(R.id.customer_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.customer_drawer_layout);
        NavigationView navigationView = findViewById(R.id.customer_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (navigationView != null) {
            // Get the header view from the NavigationView
            android.view.View headerView = navigationView.getHeaderView(0);

            // Find the TextView inside the header view
            TextView headerText = headerView.findViewById(R.id.customer_siderbar_header_text);
            headerText.setText(user.getFirstName()+" "+user.getLastName());
//                headerText.setText("Welcome, Customer!"); // Set the desired text
            // Modify the text of the TextView
//            if (headerText != null) {
//            }
        }


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

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.sidebar_option_home) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_home_customer()).commit();
//        } else if (item.getItemId() == R.id.sidebar_option_about_us) { // About Us
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_about_us()).commit();
//        } else if (item.getItemId() == R.id.sidebar_option_profile) { // Profile
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UpdateProfile()).commit();
//        } else if (item.getItemId() == R.id.sidebar_option_appointment) { // Book Appointment
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_barbershop_list()).commit();
//        } else if (item.getItemId() == R.id.sidebar_option_chat) { // Chat with Barber
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_chat_with_barber()).commit();
//        } else if (item.getItemId() == R.id.sidebar_option_rate) { // Rate Barbershop
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_rate_barbershop()).commit();
//        } else if (item.getItemId() == R.id.sidebar_option_find_barbershops) { // Find Barbershops
//            startActivity(new Intent(CustomerPage.this, MapsActivity.class));
//        } else if (item.getItemId() == R.id.sidebar_option_signout) { // Sign Out
//            new FragmentSignOutDialog().show(getSupportFragmentManager(), "SignOutDialog");
////            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentSignOutDialog()).commit();
//        }
//
//        drawerLayout.closeDrawer(GravityCompat.START);
//        return true;
//    }
@Override
public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment fragment = null;
    String tag = null;
    if (item.getItemId() == R.id.sidebar_option_home) {
        tag = "HOME";
        fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = new fragment_home_customer();
        }
    } else if (item.getItemId() == R.id.sidebar_option_about_us) {
        tag = "ABOUT_US";
        fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = new fragment_about_us();
        }
    } else if (item.getItemId() == R.id.sidebar_option_profile) {
        tag = "PROFILE";
        fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = new UpdateProfile();
        }
    } else if (item.getItemId() == R.id.sidebar_option_appointment) {
        tag = "BARBERSHOP_LIST";
        fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = new fragment_barbershop_list();
        }
    } else if (item.getItemId() == R.id.sidebar_option_chat) {
        tag = "CHAT";
        fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = new fragment_chat_with_barber();
        }
    } else if (item.getItemId() == R.id.sidebar_option_signout) {
        // Show the sign-out dialog as a dialog, not a fragment
        new FragmentSignOutDialog().show(getSupportFragmentManager(), "SignOutDialog");
        drawerLayout.closeDrawer(GravityCompat.START);
        return true; // Return early since no fragment is being replaced
    }

    // Continue with other menu options...

    if (tag != null) {
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment, tag).commit();
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
