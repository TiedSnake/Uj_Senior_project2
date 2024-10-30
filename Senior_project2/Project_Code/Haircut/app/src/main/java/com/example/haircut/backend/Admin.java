package com.example.haircut.backend;

import java.util.HashSet;

public class Admin extends User {
    private final HashSet<User> users_blacklist;
    private final HashSet<Review> reviews_list;

    public Admin(String firstName, String lastName, String email) {
        //Pass the user type optionally upon Admin object creation.
        super(firstName, lastName, email, UserType.ADMIN);
        this.users_blacklist = new HashSet<>();
        this.reviews_list = new HashSet<>();
    }
    
    public String viewUsersBlacklist() {
        StringBuilder sb = new StringBuilder();
        for (User user : this.users_blacklist)
            sb.append(user.getUuid().toString()).append("\n");
        return sb.toString();
    }

    public boolean addToBlacklist(User user) {
        return this.users_blacklist.add(user);
    }

    public boolean removeFromBlacklist(User user) {
        return this.users_blacklist.remove(user);
    }

    public String viewReviewList() {
        StringBuilder sb = new StringBuilder();
        for (Review review : this.reviews_list)
            sb.append(review.getUuid().toString()).append("\n");
        return sb.toString();
    }

    public boolean removeReview(Review review) {
        return this.reviews_list.remove(review);
    }
}
