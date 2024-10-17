package com.example.haircut;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class fragment_sign_out extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_sign_out, container, false);

        // Handle sign out button
        Button signOutButton = view.findViewById(R.id.confirmSignOutButton);
        signOutButton.setOnClickListener(v -> {
            // Here, handle the sign out logic (e.g., clear user session, logout)
            signOutUser();
        });

        return view;
    }

    // Method to handle sign out and navigate to the welcome page
    private void signOutUser() {
        // Perform sign out logic, such as clearing shared preferences or user session
        // After signing out, redirect to the Welcome Page (assuming it's an Activity)
        Intent intent = new Intent(getActivity(), welcome.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
