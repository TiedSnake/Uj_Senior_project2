package com.haircut.frontend;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.haircut.R;

public class fragment_rate_barbershop extends Fragment {

    private TextView barbershopNameTextView; // TextView to display barbershop name
    private RatingBar ratingBar;
    private EditText reviewEditText;
    private Button submitButton;
    private String selectedBarbershopName;

    // Updated list of barbershops in order
    private final String[] barbershopNames = {
            "Sheraton Barbershop",
            "Capo Salon",
            "90 Seconds Salon",
            "Maria Salon",
            "King of Shaves Salon",
            "Lava Salon"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment_rate_barbershop.xml layout
        View view = inflater.inflate(R.layout.fragment_rate_barbershop, container, false);

        // Initialize views
        barbershopNameTextView = view.findViewById(R.id.barbershopNameTextView);
        ratingBar = view.findViewById(R.id.ratingBar);
        reviewEditText = view.findViewById(R.id.editTextReview);
        submitButton = view.findViewById(R.id.buttonSubmit);

        // Prompt user to select a barbershop initially
        selectBarbershop();

        // Set up submit button
        submitButton.setOnClickListener(v -> {
            if (selectedBarbershopName == null) {
                Toast.makeText(getContext(), "Please select a barbershop first.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get rating and review
            float rating = ratingBar.getRating();
            String review = reviewEditText.getText().toString();

            if (review.isEmpty()) {
                Toast.makeText(getContext(), "Please write a review.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Simulate saving the rating and review (replace with database logic)
            Toast.makeText(getContext(), "Review submitted for " + selectedBarbershopName, Toast.LENGTH_SHORT).show();

            // Display the review
            barbershopNameTextView.setText("Barbershop: " + selectedBarbershopName + "\nRating: " + rating + " stars\nReview: " + review);

            // Reset inputs
            ratingBar.setRating(0);
            reviewEditText.setText("");
        });

        return view;
    }

    // Method to prompt the user to select a barbershop
    private void selectBarbershop() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select a Barbershop");

        builder.setItems(barbershopNames, (dialog, which) -> {
            // Update the selected barbershop
            selectedBarbershopName = barbershopNames[which];
            barbershopNameTextView.setText(selectedBarbershopName);
        });

        builder.setCancelable(false);
        builder.show();
    }
}
