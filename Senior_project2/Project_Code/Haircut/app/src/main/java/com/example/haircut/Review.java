package com.example.haircut;

import com.example.haircut.backend.User;

import java.util.UUID;

public class Review {
    private final UUID uuid;
    //Review text.
    private String review_content;

    //barber rated by customer
    private String rating;

    //User of type Customer
    private User reviewer;

    //User of type Barber
    private User Reviewee;

    public Review(String review_content, String rating, User reviewer, User reviewee) {
        this.uuid = UUID.randomUUID();
        this.review_content = review_content;
        this.rating = rating;
        this.reviewer = reviewer;
        Reviewee = reviewee;
    }

    public String getReview_content() {
        return review_content;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setReview_content(String review_content) {
        this.review_content = review_content;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public User getReviewer() {
        return reviewer;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    public User getReviewee() {
        return Reviewee;
    }

    public void setReviewee(User reviewee) {
        Reviewee = reviewee;
    }
}
