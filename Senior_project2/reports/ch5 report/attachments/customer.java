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

public class customer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_page); //getting layout needed

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //getting the toolbar (sidebar) of the page

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open, R.string.close); // to handle open and close of toolbar(sidebar)
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            // Default fragment when the app first opens (Home)
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_home_customer()).commit();
            navigationView.setCheckedItem(R.id.sidebarOptionHome);
            toolbar.setTitle(R.string.sidebar_headerText); // Set initial title to Home
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.sidebarOptionHome) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_home_customer()).commit();
            toolbar.setTitle(R.string.sidebar_headerText); // Title for Home page (Header)
        } else if (item.getItemId() == R.id.sidebarOption1) { // About Us
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_about_us()).commit();
            toolbar.setTitle(R.string.About_us); // Title for About us page (Header)
        } else if (item.getItemId() == R.id.sidebarOption2) { // Profile
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new profile1()).commit();
            toolbar.setTitle(R.string.profile_customer);// Title for Profile page (Header)
        } else if (item.getItemId() == R.id.sidebarOption3) { // Book Appointment
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_barbershop_list()).commit();
            toolbar.setTitle(R.string.book_an_appointment);// Title for Book Appointment page (Header)
        } else if (item.getItemId() == R.id.sidebarOption4) { // Chat with Barber
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_chat_with_barber()).commit();
            toolbar.setTitle(R.string.chat_with_barber);// Title for Chat with Barber page (Header)
        } else if (item.getItemId() == R.id.sidebarOption5) { // Sign Out
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_sign_out()).commit();
            toolbar.setTitle(R.string.sign_out_button_customer);// Title for sign out page (Header)
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
