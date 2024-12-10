package com.haircut.frontend.barber;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.haircut.frontend.customer.Appointment;
import com.haircut.databinding.BarberAppointmentItemBinding;

public class BarberAppointmentsAdapter extends RecyclerView.Adapter<BarberAppointmentsAdapter.ViewHolder> {

    private final OnAppointmentDeclined onAppointmentDeclined;
    private final AsyncListDiffer<Appointment> differ;

    public BarberAppointmentsAdapter(OnAppointmentDeclined onAppointmentDeclined) {
        this.onAppointmentDeclined = onAppointmentDeclined;

        // DiffUtil setup
        DiffUtil.ItemCallback<Appointment> diffCallback = new DiffUtil.ItemCallback<Appointment>() {
            @Override
            public boolean areItemsTheSame(@NonNull Appointment oldItem, @NonNull Appointment newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @SuppressLint("DiffUtilEquals")
            @Override
            public boolean areContentsTheSame(@NonNull Appointment oldItem, @NonNull Appointment newItem) {
                return oldItem.equals(newItem);
            }
        };
        differ = new AsyncListDiffer<>(this, diffCallback);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BarberAppointmentItemBinding binding = BarberAppointmentItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment currentItem = differ.getCurrentList().get(position);
        holder.onBind(currentItem);
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    public AsyncListDiffer<Appointment> getDiffer() {
        return differ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final BarberAppointmentItemBinding binding;

        public ViewHolder(@NonNull BarberAppointmentItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBind(Appointment data) {
            binding.customerNameTextView.setText("Customer: " + data.getCustomer());
            binding.timeTextView.setText("Time: " + data.getStart() + " PM:" + (Integer.parseInt(String.valueOf(data.getStart())) + 1) + " PM");
            binding.dateTextView.setText("Date: " + data.getDate());
            binding.isReservedTextView.setText("Reserved");

            binding.acceptAppointmentButton.setOnClickListener(v ->
                    binding.buttonsLayout.setVisibility(ViewGroup.GONE)
            );

            binding.declineAppointmentButton.setOnClickListener(v -> {
                onAppointmentDeclined.onAppointmentDeclined(Integer.parseInt(String.valueOf(data.getId())));
                binding.buttonsLayout.setVisibility(ViewGroup.GONE);
            });
        }
    }
}

