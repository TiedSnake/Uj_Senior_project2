package com.haircut.frontend.customer;

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
import com.haircut.frontend.shared.FragmentSignOutDialog;
import com.haircut.frontend.shared.UpdateProfile;
import com.haircut.frontend.shared.fragment_chat;

public class CustomerPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_page);
        User user = Service.getCurrentUser();
        // Set up the toolbar and make it the action bar
        Toolbar toolbar = findViewById(R.id.customer_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.customer_drawer_layout);
        NavigationView navigationView = findViewById(R.id.customer_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (navigationView != null) {
            // Get the header view from the NavigationView
            android.view.View headerView = navigationView.getHeaderView(0);

            // Find the TextView inside the header view
            TextView headerText = headerView.findViewById(R.id.customer_sidebar_header_text);
//            headerText.setText(user.getFirstName() + " " + user.getLastName());
        }
        // Set up the toggle for the navigation drawer with the toolbar
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.teal_200)); // Sets icon color
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            // Default fragment when the app first opens (Home)
            getSupportFragmentManager().beginTransaction().replace(R.id.customer_frame_layout, new fragment_home_customer()).commit();
            navigationView.setCheckedItem(R.id.customer_sidebar_option_home);
        }
    }

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = null;
            String tag = null;
            if (item.getItemId() == R.id.customer_sidebar_option_home) {
                tag = "HOME";
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = new fragment_home_customer();
                }
            } else if (item.getItemId() == R.id.customer_sidebar_option_about_us) {
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
                    fragment = new fragment_chat();
                }
            } else if (item.getItemId() == R.id.sidebar_option_rate) {
                tag = "RATE";
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = new fragment_rate_barbershop();
                }
            } else if (item.getItemId() == R.id.sidebar_option_find_barbershops) {
                startActivity(new Intent(CustomerPage.this, MapsActivity.class));
                return true;
            } else if (item.getItemId() == R.id.sidebar_option_signout) {
                FragmentSignOutDialog signOutDialog = (FragmentSignOutDialog) getSupportFragmentManager().findFragmentByTag("SignOutDialog");

                if (signOutDialog != null && signOutDialog.isVisible()) {
                    // If dialog is already showing, dismiss it before showing a new one
                    signOutDialog.dismiss();
                }

                // Show the sign-out dialog
                new FragmentSignOutDialog().show(getSupportFragmentManager(), "SignOutDialog");
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        if (tag != null) {//if tag is not null display the clicked option.
            fragmentManager.beginTransaction().replace(R.id.customer_frame_layout, fragment, tag).commit();
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
