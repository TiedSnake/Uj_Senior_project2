package com.haircut.frontend.barber;

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

public class fragment_home_barber extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_home_barber, container, false);
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

    private List<AppointmentRecord> getIncomingAppointments() {
//        appointments.add(new Appointment("Ahmad Khalid", "Haircut", "10/18/2024", "12:30 PM"));
//        appointments.add(new Appointment("Osama Nasser", "Beard Trim", "10/19/2024", "2:00 PM"));
        return new ArrayList<>();
    }
    private List<ReviewRecord> getCustomerReviews() {
        List<ReviewRecord> reviews = new ArrayList<>();
        // Add sample reviews
        reviews.add(new ReviewRecord("Ahmad Khalid", "Great haircut!", 5));
        reviews.add(new ReviewRecord("Osama Nasser", "Very professional.", 4));
        return reviews;
    }
}
