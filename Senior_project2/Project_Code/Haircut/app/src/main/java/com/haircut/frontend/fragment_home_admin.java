package com.haircut.frontend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.haircut.R;

import java.util.ArrayList;
import java.util.List;

public class fragment_home_admin extends Fragment {

    private RecyclerView rvUsers;
    private Button tabBarber, tabClients;
    private UserAdapter userAdapter;
    private List<UserDataModel2> barbersList, clientsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_admin, container, false);

        // Initialize views
        rvUsers = view.findViewById(R.id.rvUsers);
        tabBarber = view.findViewById(R.id.tabBarber);
        tabClients = view.findViewById(R.id.tabClients);

        // Initialize user lists
        initializeUserLists();

        // Set up RecyclerView
        rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        userAdapter = new UserAdapter(new ArrayList<>());
        rvUsers.setAdapter(userAdapter);

        // Set click listeners for Barber and Customer tabs
        tabBarber.setOnClickListener(v -> showUsers(barbersList));
        tabClients.setOnClickListener(v -> showUsers(clientsList));

        // Add button for viewing ratings/reviews
        Button btnViewRatings = view.findViewById(R.id.btnViewRatings);  // Ensure this ID is defined in the fragment layout
        btnViewRatings.setOnClickListener(v -> {
            // Load the fragment to view ratings/reviews
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.admin_frame_layout, new fragment_view_ratings())
                    .addToBackStack(null) // Optional: add to back stack to allow back navigation
                    .commit();
        });

        return view;
    }

    private void initializeUserLists() {
        // Sample Barber Data
        barbersList = new ArrayList<>();
        barbersList.add(new UserDataModel2("Barber 1", "View", "Block", "Delete"));
        barbersList.add(new UserDataModel2("Barber 2", "View", "Block", "Delete"));

        // Sample Customer Data
        clientsList = new ArrayList<>();
        clientsList.add(new UserDataModel2("Customer 1", "View", "Block", "Delete"));
        clientsList.add(new UserDataModel2("Customer 2", "View", "Block", "Delete"));
    }

    private void showUsers(List<UserDataModel2> users) {
        userAdapter.updateUsers(users);
    }
}
