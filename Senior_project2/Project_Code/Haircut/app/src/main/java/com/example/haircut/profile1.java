package com.example.haircut;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class profile1 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_profile1, container, false);
        Toolbar toolbar=requireActivity().findViewById(R.id.sidebar_headerText);
        if (toolbar != null) {
            toolbar.setTitle("Profile");
        }


        // Handle update button to update profile and return to barber_page
        Button updateButton = view.findViewById(R.id.update_profile);
        updateButton.setOnClickListener(v -> {
            // Perform the update logic here (e.g., save profile information)
            Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_SHORT).show();

            // After updating, pop the current fragment and go back to barber_page
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }
}
