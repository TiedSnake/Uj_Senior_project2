package com.example.haircut;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class fragment_barbershop_list extends Fragment {

    private TextView selectedBarbershop; // To track the selected barbershop

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment_barbershop_list.xml layout
        View view = inflater.inflate(R.layout.fragment_barbershop_list, container, false);

        // Initialize all TextViews (Barbershop names)
        TextView barbershop1 = view.findViewById(R.id.barbershop1);
        TextView barbershop2 = view.findViewById(R.id.barbershop2);
        TextView barbershop3 = view.findViewById(R.id.barbershop3);
        TextView barbershop4 = view.findViewById(R.id.barbershop4);
        TextView barbershop5 = view.findViewById(R.id.barbershop5);
        TextView barbershop6 = view.findViewById(R.id.barbershop6);

        // Set click listeners for barbershop names
        barbershop1.setOnClickListener(barbershopClickListener);
        barbershop2.setOnClickListener(barbershopClickListener);
        barbershop3.setOnClickListener(barbershopClickListener);
        barbershop4.setOnClickListener(barbershopClickListener);
        barbershop5.setOnClickListener(barbershopClickListener);
        barbershop6.setOnClickListener(barbershopClickListener);

        // Initialize "See Appointments" button
        Button seeAppointmentsButton = view.findViewById(R.id.see_appointments_button);
        seeAppointmentsButton.setOnClickListener(v -> {
            if (selectedBarbershop != null) {
                // Navigate to the appointments fragment
                navigateToAppointments();
            } else {
                // Show a message if no barbershop is selected
//                Toast.makeText(getActivity(), "Please select a barbershop first.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    // OnClickListener for barbershop names
    private final View.OnClickListener barbershopClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // If a barbershop is already selected, remove its highlight
            if (selectedBarbershop != null) {
                selectedBarbershop.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.transparent));
            }

            // Set the clicked barbershop as the selected one and highlight it
            selectedBarbershop = (TextView) v;
            selectedBarbershop.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.teal_200)); // Highlight color
        }
    };

    // Method to navigate to the appointments fragment
    private void navigateToAppointments() {
        Fragment fragment = new fragment_appointment_customer(); // Replace with your appointments fragment class
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment) // Replace with the main container ID in your activity
                .addToBackStack(null) // Optional, adds the transaction to the back stack
                .commit();
    }

}
