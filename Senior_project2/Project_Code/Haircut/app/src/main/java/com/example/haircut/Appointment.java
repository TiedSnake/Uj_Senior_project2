package com.example.haircut;

public class Appointment {
    private String name;
    private String service;
    private String date;
    private String time;

    // Constructor that accepts 4 arguments
    public Appointment(String name, String service, String date, String time) {
        this.name = name;
        this.service = service;
        this.date = date;
        this.time = time;
    }

    // Getters and setters (optional)
    public String getName() {
        return name;
    }

    public String getService() {
        return service;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
