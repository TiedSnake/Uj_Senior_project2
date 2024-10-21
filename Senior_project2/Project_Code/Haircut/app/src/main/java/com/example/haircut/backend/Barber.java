package com.example.haircut.backend;

import com.example.haircut.Review;

import java.util.HashSet;

public class Barber extends User {
    private final HashSet<com.example.haircut.Review> reviews;
    private final HashSet<com.example.haircut.backend.Appointment> appointments;

    public boolean acceptAppointment(com.example.haircut.backend.Appointment appointment) {
        /*
         * Modifies the status variable in the passed appointment object to accepted & adds it to the appointment set
         */
        appointment.setStatus("accepted");
        return appointments.add(appointment);
    }

    public boolean rejectAppointment(com.example.haircut.backend.Appointment appointment) {
        /*
         * Modifies the status variable in the passed appointment object to rejected & adds it to the appointment set
         */
        appointment.setStatus("rejected");
        return appointments.add(appointment);
    }
    public String viewReviewList() {
        StringBuilder sb = new StringBuilder();
        for (com.example.haircut.Review review : this.reviews)
            sb.append(review.getUuid().toString()).append("\n");
        return sb.toString();
    }

    public Barber(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password, Barber.class.getName());
        this.reviews = new HashSet<Review>();
        this.appointments = new HashSet<Appointment>();
    }

}
