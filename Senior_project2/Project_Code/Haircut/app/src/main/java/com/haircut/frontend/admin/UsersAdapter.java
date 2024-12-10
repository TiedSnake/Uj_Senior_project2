package com.haircut.frontend.admin;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.haircut.R;
import com.haircut.databinding.UserItemBinding;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private final OnUserClickListener onUserClickListener;
    final AsyncListDiffer<FirebaseUser> differ;

    public UsersAdapter(OnUserClickListener onUserClickListener) {
        this.onUserClickListener = onUserClickListener;
        this.differ = new AsyncListDiffer<>(this, new DiffUtil.ItemCallback<FirebaseUser>() {
            @Override
            public boolean areItemsTheSame(FirebaseUser oldItem, FirebaseUser newItem) {
                return oldItem.equals(newItem); // Adjust as necessary
            }

            @SuppressLint("DiffUtilEquals")
            @Override
            public boolean areContentsTheSame(FirebaseUser oldItem, FirebaseUser newItem) {
                return oldItem.equals(newItem); // Adjust as necessary
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final UserItemBinding binding;

        public ViewHolder(UserItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        public void onBind(FirebaseUser data) {

            binding.userNameTextView.setText(data.getFirstName() + " " + data.getLastName());
            binding.emailTextView.setText(data.getEmail());
            binding.userTypeTextView.setText(data.getUserType());

            binding.isBlockedButton.setText(data.isBlocked() ? "Unblock" : "Block");
            binding.isBlockedButton.setBackgroundColor(
                    data.isBlocked() ? itemView.getContext().getResources().getColor(R.color.red)
                            : itemView.getContext().getResources().getColor(R.color.green)
            );
            binding.isBlockedButton.setOnClickListener(v -> {
                onUserClickListener.onBlockButtonClicked(data.getUuid(), !data.isBlocked());
            });

            binding.ratingBar.setVisibility(data.getRating() != 0.0 ? View.VISIBLE : View.INVISIBLE);
            binding.ratingBar.setRating((float) data.getRating());

        }
    }

    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        UserItemBinding view = UserItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UsersAdapter.ViewHolder holder, int position) {
        FirebaseUser currentItem = differ.getCurrentList().get(position);
        holder.onBind(currentItem);
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    public void submitList(List<FirebaseUser> list) {
        differ.submitList(list);
    }

    public AsyncListDiffer<FirebaseUser> getDiffer() {
        return differ;
    }
}

