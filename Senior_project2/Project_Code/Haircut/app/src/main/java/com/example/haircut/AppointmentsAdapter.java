package com.example.haircut;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.haircut.backend.Appointment;

import java.util.List;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.ViewHolder> {

    private List<Appointment> appointmentsList;

    public AppointmentsAdapter(List<Appointment> appointmentsList) {
        this.appointmentsList = appointmentsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment appointment = appointmentsList.get(position);
        holder.customerName.setText(appointment.getName());
        holder.date.setText(appointment.getDate());
        holder.time.setText(appointment.getTime());
    }

    @Override
    public int getItemCount() {
        return appointmentsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView customerName, date, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customerName);
            date = itemView.findViewById(R.id.appointmentDate);
            time = itemView.findViewById(R.id.appointmentTime);
        }
    }
}
