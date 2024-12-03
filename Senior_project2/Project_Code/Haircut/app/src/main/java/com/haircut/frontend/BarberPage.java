package com.haircut.frontend;

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

public class BarberPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barber_page);
        User user = Service.getCurrentUser();
        Toolbar toolbar = findViewById(R.id.barber_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.barber_drawer_layout);
        navigationView = findViewById(R.id.barber_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (navigationView != null) {
            android.view.View headerView = navigationView.getHeaderView(0);

            // Finds the TextView inside the header view
            TextView headerText = headerView.findViewById(R.id.barber_siderbar_header_text);
            // TODO: 12/3/24 undo this
//            headerText.setText(user.getFirstName()+" "+user.getLastName());
        }
        // Set up the toggle for the navigation drawer with the toolbar
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
//        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.lightRed)); // changes the color of the options menu icon in sidebar
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Sets up the ActionBarDrawerToggle
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.gray)); // Sets icon color
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState(); // Sync the toggle state with the drawer


        // Setting the navigation icon for the Toolbar
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            toolbar.setNavigationIcon(R.drawable.ic_menu); // loads the icon in sidebar
//        }
//


        // Load default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.barber_frame_layout, new fragment_home_barber()).commit();
            navigationView.setCheckedItem(R.id.barber_sidebar_option_home);
        }
    }

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = null;
            String tag = null;
            if (item.getItemId() == R.id.barber_sidebar_option_home) {
                tag = "HOME";
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = new fragment_home_barber();
                }
            } else if (item.getItemId() == R.id.barber_sidebar_option_profile) {
                tag = "BARBER_PROFILE";
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = new UpdateProfile();
                }
            } else if (item.getItemId() == R.id.barber_sidebar_option_services_menu) {
                tag = "SERVICES_MENU";
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = new menu();
                }
            } else if (item.getItemId() == R.id.barber_sidebar_option_edit_services_menu) {
                tag = "EDIT_SERVICES_MENU";
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = new fragment_edit_service_menu();
                }
            } else if (item.getItemId() == R.id.barber_sidebar_option_appointments) {
                tag = "APPOINTMENTS";
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = new fragment_appointment();
                }
            } else if (item.getItemId() == R.id.barber_sidebar_option_chat) {
                tag = "CHAT";
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = new fragment_chat();
                }
            } else if (item.getItemId() == R.id.barber_sidebar_option_reviews) {
                tag = "REVIEWS";
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = new fragment_reviews();
                }
            } else if (item.getItemId() == R.id.barber_sidebar_option_signout) {
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
                fragmentManager.beginTransaction().replace(R.id.barber_frame_layout, fragment, tag).commit();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }

//    private void signOut() {
//        // Clear session data if necessary (e.g., shared preferences or any auth token)
//        // Then, navigate to the WelcomePage activity
//        Intent intent = new Intent(BarberPage.this, WelcomePage.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
//        startActivity(intent);
//        Toast.makeText(this, "Signed out successfully!", Toast.LENGTH_SHORT).show();
//        finish(); // Close current activity
//    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
