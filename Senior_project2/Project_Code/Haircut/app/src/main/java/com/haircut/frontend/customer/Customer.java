package com.haircut.frontend.customer;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.haircut.R;
import java.util.ArrayList;
import java.util.List;

public class Customer extends AppCompatActivity implements OnAppointmentClicked {

    private AppointmentsAdapter appointmentsAdapter;
    private RecyclerView recyclerView;
    private List<Appointment> list = new ArrayList<>();
    private CustomerViewModel viewModel;
    private String name = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer);

        recyclerView = findViewById(R.id.appointmentsRecyclerView);

        appointmentsAdapter = new AppointmentsAdapter(this);
        viewModel = new ViewModelProvider(this).get(CustomerViewModel.class);

        Log.d("hamzaLo", "getting the list ...toString()");

        name = getIntent().getStringExtra("name");

        // Fetch Appointments
        viewModel.getAppointments();

        if (recyclerView != null) {
            recyclerView.setAdapter(appointmentsAdapter);
        }

        // Observe LiveData
        viewModel.appointments.observe(this, appointments -> {
            if (appointments != null) {
                list = new ArrayList<>(appointments);
                appointmentsAdapter.getDiffer().submitList(list);
                Log.d("hamza", "Appointments: " + list);
            }
        });

        viewModel.errorMessage.observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, "Error: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onAppointmentClicked(int index, boolean isReserved, String sName) {
        if (isReserved) {
            viewModel.updateIsReserved(index, isReserved, name);
        } else {
            viewModel.updateIsReserved(index, isReserved, "");
        }
    }
}

