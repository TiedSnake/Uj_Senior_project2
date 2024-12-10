package com.haircut.frontend.customer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.haircut.R;
import com.haircut.databinding.AppointmentItemBinding;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.ViewHolder> {

    private final OnAppointmentClicked onAppointmentClicked;

    public AppointmentsAdapter(OnAppointmentClicked onAppointmentClicked) {
        this.onAppointmentClicked = onAppointmentClicked;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final AppointmentItemBinding binding;

        public ViewHolder(AppointmentItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBind(Appointment data, OnAppointmentClicked onAppointmentClicked) {
            Context context = itemView.getContext();
            binding.barberNameTextView.setText("Barber: " + data.getBarberName());
            binding.timeTextView.setText("Time: " + data.getStart() + " PM:" + (data.getStart() + 1) + " PM");
            binding.dateTextView.setText("Date: " + data.getDate());

            if (data.isReserved()) {
                binding.isReservedTextView.setText("Reserved");
                binding.reserveAppointmentButton.setText("Cancel");
                binding.reserveAppointmentButton.setBackgroundColor(context.getResources().getColor(R.color.red));
                binding.isReservedTextView.setTextColor(context.getResources().getColor(R.color.red));
            } else {
                binding.isReservedTextView.setText("Not Reserved");
                binding.reserveAppointmentButton.setText("Reserve");
                binding.reserveAppointmentButton.setBackgroundColor(context.getResources().getColor(R.color.green));
                binding.isReservedTextView.setTextColor(context.getResources().getColor(R.color.green));
            }

            binding.reserveAppointmentButton.setOnClickListener(v ->
                    onAppointmentClicked.onAppointmentClicked(getAdapterPosition(), !data.isReserved(), "")
            );
        }
    }

    private final DiffUtil.ItemCallback<Appointment> diffCallback = new DiffUtil.ItemCallback<Appointment>() {
        @Override
        public boolean areItemsTheSame(@NonNull Appointment oldItem, @NonNull Appointment newItem) {
            return oldItem.equals(newItem);
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull Appointment oldItem, @NonNull Appointment newItem) {
            return oldItem.equals(newItem);
        }
    };

    private final AsyncListDiffer<Appointment> differ = new AsyncListDiffer<>(this, diffCallback);

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AppointmentItemBinding view = AppointmentItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment currentItem = differ.getCurrentList().get(position);
        holder.onBind(currentItem, onAppointmentClicked);
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    public AsyncListDiffer<Appointment> getDiffer() {
        return differ;
    }
}

