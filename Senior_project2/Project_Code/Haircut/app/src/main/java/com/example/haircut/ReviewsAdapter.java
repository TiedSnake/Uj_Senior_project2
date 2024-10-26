package com.example.haircut;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.haircut.backend.Review;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private List<Review> reviewsList;

    public ReviewsAdapter(List<Review> reviewsList) {
        this.reviewsList = reviewsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = reviewsList.get(position);
        holder.customerName.setText(review.getCustomerName());
        holder.reviewText.setText(review.getReviewText());
        holder.ratingBar.setRating(review.getRating());
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView customerName, reviewText;
        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customerName);
            reviewText = itemView.findViewById(R.id.reviewText);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}
