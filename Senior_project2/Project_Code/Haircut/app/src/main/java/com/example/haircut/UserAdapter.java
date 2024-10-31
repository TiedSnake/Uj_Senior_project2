package com.example.haircut;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<UserDataModel2> userList;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public UserAdapter(List<UserDataModel2> userList) {
        this.userList = userList;
    }

    public void updateUsers(List<UserDataModel2> users) {
        this.userList = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item_layout, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserDataModel2 user = userList.get(position);
        holder.tvUserName.setText(user.getName());
        holder.btnView.setText(user.getViewAction());
        holder.btnBlock.setText(user.getBlockAction());
        holder.btnDelete.setText(user.getDeleteAction());

        // Set buttons' visibility based on the selected position
        if (position == selectedPosition) {
            holder.btnView.setVisibility(View.VISIBLE);
            holder.btnBlock.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);
        } else {
            holder.btnView.setVisibility(View.INVISIBLE);
            holder.btnBlock.setVisibility(View.INVISIBLE);
            holder.btnDelete.setVisibility(View.INVISIBLE);
        }

        // Toggle visibility on item click
        holder.itemView.setOnClickListener(v -> {
            selectedPosition = (position == selectedPosition) ? RecyclerView.NO_POSITION : position;
            notifyDataSetChanged();
        });

        // Implement button actions if needed
        holder.btnView.setOnClickListener(v -> {
            // Handle "View" action
        });

        holder.btnBlock.setOnClickListener(v -> {
            // Handle "Block" action
        });

        holder.btnDelete.setOnClickListener(v -> {
            // Handle "Delete" action
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName;
        Button btnView, btnBlock, btnDelete;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            btnView = itemView.findViewById(R.id.btnView);
            btnBlock = itemView.findViewById(R.id.btnBlock);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            // Set buttons to be initially invisible
            btnView.setVisibility(View.INVISIBLE);
            btnBlock.setVisibility(View.INVISIBLE);
            btnDelete.setVisibility(View.INVISIBLE);
        }
    }
}
