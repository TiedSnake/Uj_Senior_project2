package com.haircut.frontend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.haircut.R;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_home2, container, false);

        // Setup RecyclerView for Incoming Appointments
        RecyclerView appointmentsRecyclerView = view.findViewById(R.id.incomingAppointmentsRecyclerView);
        appointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        AppointmentsAdapter appointmentsAdapter = new AppointmentsAdapter(getIncomingAppointments());
        appointmentsRecyclerView.setAdapter(appointmentsAdapter);

        // Setup RecyclerView for Customer Reviews
        RecyclerView reviewsRecyclerView = view.findViewById(R.id.customerReviewsRecyclerView);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ReviewsAdapter reviewsAdapter = new ReviewsAdapter(getCustomerReviews());
        reviewsRecyclerView.setAdapter(reviewsAdapter);

        return view;
    }

    // Mock method to get incoming appointments (replace with real data source)
    private List<AppointmentRecord> getIncomingAppointments() {
        // Add sample appointments with 4 arguments: name, service, date, and time
//        appointments.add(new Appointment("John Doe", "Haircut", "10/18/2024", "12:30 PM"));
//        appointments.add(new Appointment("Jane Smith", "Beard Trim", "10/19/2024", "2:00 PM"));
        return new ArrayList<>();
    }

    // Mock method to get customer reviews (replace with real data source)
    private List<ReviewRecord> getCustomerReviews() {
        List<ReviewRecord> reviews = new ArrayList<>();
        // Add sample reviews
        reviews.add(new ReviewRecord("John Doe", "Great haircut!", 5));
        reviews.add(new ReviewRecord("Jane Smith", "Very professional.", 4));
        return reviews;
    }
}
