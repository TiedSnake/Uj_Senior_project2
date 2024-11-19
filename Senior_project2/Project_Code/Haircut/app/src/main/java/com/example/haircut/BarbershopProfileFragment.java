package com.example.haircut;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BarbershopProfileFragment extends Fragment {

    private TextView profileName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for the profile
        View view = inflater.inflate(R.layout.fragment_barbershop_profile, container, false);
Button button = view.findViewById(R.id.see_appointments_button);
        profileName = view.findViewById(R.id.barbershop_name);
        button.setOnClickListener(view1 ->navigateToAppointments() );
        // Get the barbershop name from the bundle
        if (getArguments() != null) {
            String barbershopName = getArguments().getString("barbershop_name");
            profileName.setText(barbershopName); // Set the name dynamically
        }

        return view;
    }
    // Method to navigate to the appointments fragment
    private void navigateToAppointments() {
        Fragment fragment = new fragment_appointment_customer();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

}

