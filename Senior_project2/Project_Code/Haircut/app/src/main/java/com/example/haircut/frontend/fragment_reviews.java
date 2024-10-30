package com.example.haircut.frontend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.haircut.R;

import java.util.ArrayList;
import java.util.List;

public class fragment_reviews extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_reviews, container, false);

        // Setup RecyclerView for Reviews
        RecyclerView reviewsRecyclerView = view.findViewById(R.id.reviewsRecyclerView);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the adapter and set it to the RecyclerView
        ReviewsAdapter reviewsAdapter = new ReviewsAdapter(getCustomerReviews());
        reviewsRecyclerView.setAdapter(reviewsAdapter);

        return view;
    }

    // Mock method to get customer reviews (replace with real data source)
    private List<ReviewRecord> getCustomerReviews() {
        List<ReviewRecord> ReviewRecords = new ArrayList<>();
        // Add sample reviews
        ReviewRecords.add(new ReviewRecord("John Doe", "Great service and friendly staff!", 5));
        ReviewRecords.add(new ReviewRecord("Jane Smith", "Loved the haircut!", 4));
        ReviewRecords.add(new ReviewRecord("Michael Johnson", "Good experience overall.", 3));
        return ReviewRecords;
    }
}
