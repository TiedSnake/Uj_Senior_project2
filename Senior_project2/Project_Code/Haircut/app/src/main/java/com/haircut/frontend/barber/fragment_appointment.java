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

public class fragment_appointment extends Fragment {

    private AppointmentsAdapter appointmentsAdapter;
    List<AppointmentRecord> appointmentList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);
        RecyclerView appointmentsRecyclerView = view.findViewById(R.id.appointments_recycler_view);
        appointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        appointmentList = new ArrayList<>();
        loadAppointments();  // Method to load appointments into the list
        appointmentsAdapter = new AppointmentsAdapter(appointmentList);
        appointmentsRecyclerView.setAdapter(appointmentsAdapter);
        return view;
    }
    private void loadAppointments() {
        appointmentList.add(new AppointmentRecord("Ahmad Khalid", "Haircut", "2024-10-20", "10:00 AM"));
        appointmentList.add(new AppointmentRecord("Osama Nasser", "Beard Trim", "2024-10-21", "1:30 PM"));
        appointmentList.add(new AppointmentRecord("Anas Ali", "Haircut + Beard", "2024-10-22", "4:00 PM"));
        if (appointmentsAdapter != null) {
            appointmentsAdapter.notifyDataSetChanged();
        }
    }
}
