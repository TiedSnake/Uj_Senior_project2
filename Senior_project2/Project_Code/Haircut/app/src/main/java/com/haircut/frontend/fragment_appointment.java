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

public class fragment_appointment extends Fragment {

    private AppointmentsAdapter appointmentsAdapter;
    List<AppointmentRecord> appointmentList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);

        // Initialize the RecyclerView
        RecyclerView appointmentsRecyclerView = view.findViewById(R.id.appointments_recycler_view);

        // Set the LayoutManager (LinearLayout in this case)
        appointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the list of appointments (you might retrieve this from a database or API)
        appointmentList = new ArrayList<>();
        loadAppointments();  // Method to load appointments into the list

        // Set the Adapter
        appointmentsAdapter = new AppointmentsAdapter(appointmentList);
        appointmentsRecyclerView.setAdapter(appointmentsAdapter);

        return view;
    }

    // Sample method to populate the list with appointments
    private void loadAppointments() {
        // This is just an example, we can fetch actual data from a database, API, etc.
        appointmentList.add(new AppointmentRecord("John Doe", "Haircut", "2024-10-20", "10:00 AM"));
        appointmentList.add(new AppointmentRecord("Jane Smith", "Beard Trim", "2024-10-21", "1:30 PM"));
        appointmentList.add(new AppointmentRecord("Bob Johnson", "Haircut + Beard", "2024-10-22", "4:00 PM"));

        // Notify adapter that the data has changed (useful when data is updated dynamically)
        if (appointmentsAdapter != null) {
            appointmentsAdapter.notifyDataSetChanged();
        }
    }
}
