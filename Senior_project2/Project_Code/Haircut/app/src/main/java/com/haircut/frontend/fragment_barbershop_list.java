package com.haircut.frontend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.haircut.R;

public class fragment_barbershop_list extends Fragment {

    private TextView selectedBarbershop; // To track the selected barbershop
    private final int[] barbershopIds = {
            R.id.barbershop1, R.id.barbershop2, R.id.barbershop3,
            R.id.barbershop4, R.id.barbershop5, R.id.barbershop6
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment_barbershop_list.xml layout
        View view = inflater.inflate(R.layout.fragment_barbershop_list, container, false);

        // Loop through all barbershop TextView IDs and set listeners
        for (int id : barbershopIds) {
            TextView barbershop = view.findViewById(id);
            barbershop.setOnClickListener(barbershopClickListener);
        }

        return view;
    }

    // OnClickListener for barbershop names
    private final View.OnClickListener barbershopClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // If a barbershop is already selected, remove its highlight
            if (selectedBarbershop != null) {
                selectedBarbershop.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.white));
            }

            // Set the clicked barbershop as the selected one and highlight it
            selectedBarbershop = (TextView) v;
            selectedBarbershop.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.teal_200)); // Highlight color

            // Get the barbershop name
            String barbershopName = ((TextView) v).getText().toString();

            // Navigate to the profile page of the selected barbershop
            navigateToProfile(barbershopName);
        }
    };

    // Method to navigate to the profile of the selected barbershop
    private void navigateToProfile(String barbershopName) {
        try {
            // Pass the barbershop name to the profile fragment
            Bundle bundle = new Bundle();
            bundle.putString("barbershop_name", barbershopName);

            Fragment fragment = new BarbershopProfileFragment();
            fragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.barbershop_list_frame_layout, fragment)
                    .addToBackStack(null)
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
            // Consider displaying an error message or logging the issue
        }
    }
}
