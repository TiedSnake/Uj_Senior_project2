package com.haircut.frontend;

import static com.haircut.backend.Service.persistReview;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
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
import com.haircut.backend.Review;
import com.haircut.backend.User;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class fragment_rate_barbershop extends Fragment {

    private static final String TAG = "fragment_rate_barbershop";
    private TextView barbershopNameTextView;
    private RatingBar ratingBar;
    private EditText reviewEditText;
    private Button submitButton;
    private String selectedBarbershopName;

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
        View view = inflater.inflate(R.layout.fragment_rate_barbershop, container, false);

        barbershopNameTextView = view.findViewById(R.id.barbershopNameTextView);
        ratingBar = view.findViewById(R.id.ratingBar);
        reviewEditText = view.findViewById(R.id.editTextReview);
        submitButton = view.findViewById(R.id.buttonSubmit);

        selectBarbershop();

        submitButton.setOnClickListener(v -> {
            if (selectedBarbershopName == null) {
                Toast.makeText(getContext(), "Please select a barbershop first.", Toast.LENGTH_SHORT).show();
                return;
            }

            float rating = ratingBar.getRating();
            String reviewContent = reviewEditText.getText().toString();

            if (reviewContent.isEmpty()) {
                Toast.makeText(getContext(), "Please write a review.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a mock reviewer and reviewee for demonstration
            User reviewer = new User("reviewer@example.com", "John Doe", "UUID123"); // Replace with actual data
            User reviewee = new User("reviewee@example.com", selectedBarbershopName, "UUID456"); // Replace with actual data

            Review review = new Review(reviewContent, String.valueOf(rating), reviewer, reviewee);

            persistReview(review).thenAccept(isSaved -> {
                if (isSaved) {
                    Toast.makeText(getContext(), "Review submitted successfully!", Toast.LENGTH_SHORT).show();
                }
            }).exceptionally(ex -> {
                Throwable rootCause = ex.getCause();
                Log.e(TAG, "Error: failed to submit review due to: " + rootCause.getMessage());
                getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Error submitting review. Please try again.", Toast.LENGTH_LONG).show());
                return null;
            });

            ratingBar.setRating(0);
            reviewEditText.setText("");
            barbershopNameTextView.setText("Select a Barbershop");
            selectedBarbershopName = null;
        });

        return view;
    }

    private void selectBarbershop() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select a Barbershop");
        builder.setItems(barbershopNames, (dialog, which) -> {
            selectedBarbershopName = barbershopNames[which];
            barbershopNameTextView.setText(selectedBarbershopName);
        });
        builder.setCancelable(false);
        builder.show();
    }
}
