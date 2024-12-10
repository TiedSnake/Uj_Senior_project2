package com.haircut.frontend.admin;

import java.io.Serializable;

public class FirebaseUser implements Serializable {
    private String email;
    private String uuid;
    private String firstName;
    private boolean isLoggedIn;
    private String lastName;
    private String userType;
    private boolean isBlocked;
    private double rating;

    // Default constructor required for Firebase
    public FirebaseUser() {
        this.email = "";
        this.uuid = "";
        this.firstName = "";
        this.isLoggedIn = false;
        this.lastName = "";
        this.userType = "";
        this.isBlocked = false;
        this.rating = 0.0;
    }

    // Constructor with parameters
    public FirebaseUser(String email, String uuid, String firstName, boolean isLoggedIn, String lastName, String userType, boolean isBlocked, double rating) {
        this.email = email;
        this.uuid = uuid;
        this.firstName = firstName;
        this.isLoggedIn = isLoggedIn;
        this.lastName = lastName;
        this.userType = userType;
        this.isBlocked = isBlocked;
        this.rating = rating;
    }

    // Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    // Method to set isBlocked dynamically
    public void setIsBlocked(Object value) {
        if (value instanceof Boolean) {
            this.isBlocked = (Boolean) value; // Assign directly if it's a Boolean
        } else if (value instanceof String) {
            this.isBlocked = Boolean.parseBoolean((String) value); // Convert String to Boolean
        } else {
            this.isBlocked = false; // Default to false for unexpected types
        }
    }
}

