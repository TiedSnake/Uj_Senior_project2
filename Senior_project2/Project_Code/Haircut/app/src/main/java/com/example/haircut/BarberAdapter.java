package com.example.haircut;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BarberAdapter extends RecyclerView.Adapter<BarberAdapter.BarberViewHolder> {

    private List<String> barberList;

    public BarberAdapter(List<String> barberList) {
        this.barberList = barberList;
    }

    @NonNull
    @Override
    public BarberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_barber, parent, false);
        return new BarberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BarberViewHolder holder, int position) {
        String barberName = barberList.get(position);
        holder.barberNameTextView.setText(barberName);
    }

    @Override
    public int getItemCount() {
        return barberList.size();
    }

    public static class BarberViewHolder extends RecyclerView.ViewHolder {
        TextView barberNameTextView;

        public BarberViewHolder(@NonNull View itemView) {
            super(itemView);
            barberNameTextView = itemView.findViewById(R.id.tvBarberName);
        }
    }
}
