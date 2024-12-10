package com.haircut.frontend.admin;

import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.haircut.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdminActivity extends AppCompatActivity implements OnUserClickListener {

    private RecyclerView recyclerview;
    private TabLayout tablayout;

    private int currentTab = 0;

    private final UsersAdapter usersAdapter = new UsersAdapter(this);
    private List<FirebaseUser> usersList = new ArrayList<>();

    private AdminViewModel viewModel;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        recyclerview = findViewById(R.id.usersRecyclerView);
        tablayout = findViewById(R.id.tabLayout);

        recyclerview.setAdapter(usersAdapter);

        viewModel = new ViewModelProvider(this).get(AdminViewModel.class);
        viewModel.getUsersFromFirebase();

        // Observe users
        viewModel.users.observe(this, appointments -> {
            usersList = new ArrayList<>(appointments);

            if (tablayout.getTabAt(currentTab) != null) {
                tablayout.getTabAt(currentTab).select();
            }
            updateList();
        });

        // Observe error messages
        viewModel.errorMessage.observe(this, error ->
                Toast.makeText(this, "Error: " + error, Toast.LENGTH_LONG).show()
        );

        handleClickListeners();
    }

    private void handleClickListeners() {
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTabSelected(@NonNull TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0: // Barber
                        currentTab = 0;
                        updateList();
                        break;
                    case 1: // Customer
                        currentTab = 1;
                        updateList();
                        break;
                }
            }

            @Override
            public void onTabUnselected(@NonNull TabLayout.Tab tab) {
                // No action needed
            }

            @Override
            public void onTabReselected(@NonNull TabLayout.Tab tab) {
                // No action needed
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateList() {
        if (currentTab == 0) {
            usersAdapter.getDiffer().submitList(
                    usersList.stream()
                            .filter(user -> "BARBER".equalsIgnoreCase(user.getUserType()))
                            .collect(Collectors.toList())
            );
        } else {
            usersAdapter.getDiffer().submitList(
                    usersList.stream()
                            .filter(user -> "CUSTOMER".equalsIgnoreCase(user.getUserType()))
                            .collect(Collectors.toList())
            );
        }
    }

    @Override
    public void onBlockButtonClicked(String id, boolean isBlocked) {
        viewModel.updateIsBlocked(id, isBlocked);
        usersAdapter.notifyDataSetChanged();
    }
}
