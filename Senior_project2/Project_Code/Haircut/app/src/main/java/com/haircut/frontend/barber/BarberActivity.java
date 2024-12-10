package com.haircut.frontend.barber;


import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.haircut.R;
import com.haircut.frontend.customer.Appointment;
import com.haircut.frontend.customer.CustomerViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BarberActivity extends AppCompatActivity implements OnAppointmentDeclined {

    private RecyclerView recyclerView;
    private final BarberAppointmentsAdapter appointmentsAdapter = new BarberAppointmentsAdapter(this);
    private List<Appointment> list = new ArrayList<>();
    private CustomerViewModel viewModel;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber);

        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        recyclerView = findViewById(R.id.customersAppointmentsRecyclerView);

        // Initialize the ViewModel
        viewModel = new ViewModelProvider(this).get(CustomerViewModel.class);

        // Get the barber's name from the Intent
        String name = getIntent().getStringExtra("name");
        welcomeTextView.setText("Welcome, " + name);

        // Fetch Appointments
        viewModel.getAppointments();

        recyclerView.setAdapter(appointmentsAdapter);

        // Observe LiveData for Appointments
        viewModel.appointments.observe(this, appointments -> {
            list = appointments.stream()
                    .filter(appointment ->
                            appointment.getCustomer() != null &&
                                    !appointment.getCustomer().isEmpty() &&
                                    appointment.isReserved() &&
                                    name.equals(appointment.getBarberName()))
                    .collect(Collectors.toList());

            appointmentsAdapter.getDiffer().submitList(list);
            Log.d("hamza", "Appointments: " + list);
        });

        // Observe LiveData for Errors
        viewModel.errorMessage.observe(this, error ->
                Toast.makeText(this, "Error: " + error, Toast.LENGTH_LONG).show()
        );
    }

    @Override
    public void onAppointmentDeclined(int id) {
        viewModel.declineAppointment(id);
    }
}

