package com.haircut.frontend.customer;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Appointment implements Serializable {

    private long id = 0;
    private String barberName = "";
    private long start = 0;
    private boolean isReserved = false;
    private String customer = "";
    private String date = getCurrentDate(); // Initialize with the current date

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBarberName() {
        return barberName;
    }

    public void setBarberName(String barberName) {
        this.barberName = barberName;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // Custom method to handle setting isReserved
    public void setIsReserved(Object value) {
        if (value instanceof Boolean) {
            isReserved = (Boolean) value; // If it's already a Boolean, assign it directly
        } else if (value instanceof String) {
            isReserved = Boolean.parseBoolean((String) value); // If it's a String, convert to Boolean
        } else {
            isReserved = false; // Default to false if the type is unexpected
        }
    }

    // Static method to get the current date in the desired format
    public static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(new Date()); // Format the current date
    }
}

