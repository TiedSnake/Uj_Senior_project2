package com.haircut.frontend.barber;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.haircut.R;

import java.util.List;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.ViewHolder> {

    private final List<AppointmentRecord> appointmentRecords;
    private int selectedPosition = RecyclerView.NO_POSITION;
    public AppointmentsAdapter(List<AppointmentRecord> appointmentRecords) {
        this.appointmentRecords = appointmentRecords;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppointmentRecord records = appointmentRecords.get(position);
        holder.customerName.setText(records.name());
        holder.date.setText(records.date());
        holder.time.setText(records.time());

        // Set visibility of Accept and Reject buttons based on selected position
        if (position == selectedPosition) {
            holder.btnAccept.setVisibility(View.VISIBLE);
            holder.btnReject.setVisibility(View.VISIBLE);
        } else {
            holder.btnAccept.setVisibility(View.INVISIBLE);
            holder.btnReject.setVisibility(View.INVISIBLE);
        }

        // Toggle visibility on item click
        holder.itemView.setOnClickListener(v -> {
            selectedPosition = (position == selectedPosition) ? RecyclerView.NO_POSITION : position;
            notifyDataSetChanged();
        });

        // Implement button actions for "Accept" and "Reject"
        holder.btnAccept.setOnClickListener(v -> {
            // Handle "Accept" action for the selected appointment
        });

        holder.btnReject.setOnClickListener(v -> {
            // Handle "Reject" action for the selected appointment
        });
    }

    @Override
    public int getItemCount() {
        return appointmentRecords.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView customerName, date, time;
        Button btnAccept, btnReject;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customerName);
            date = itemView.findViewById(R.id.appointmentDate);
            time = itemView.findViewById(R.id.appointmentTime);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);

            // Set buttons to be initially invisible
            btnAccept.setVisibility(View.INVISIBLE);
            btnReject.setVisibility(View.INVISIBLE);
        }
    }
}
