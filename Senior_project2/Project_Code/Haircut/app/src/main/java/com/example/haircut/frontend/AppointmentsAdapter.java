package com.example.haircut.frontend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.haircut.R;

import java.util.List;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.ViewHolder> {

    private final List<AppointmentRecord> appointmentRecords;

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
    }

    @Override
    public int getItemCount() {
        return appointmentRecords.size();
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
