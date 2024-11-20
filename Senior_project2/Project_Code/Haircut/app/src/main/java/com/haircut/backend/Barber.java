package com.haircut.backend;

import java.util.HashSet;

public class Barber extends User {
    private final HashSet<Review> reviews;
    private final HashSet<Appointment> appointments;

    public boolean acceptAppointment(Appointment appointment) {
        /*
         * Modifies the status variable in the passed appointment object to accepted & adds it to the appointment set
         */
        appointment.setStatus("accepted");
        return appointments.add(appointment);
    }

    public boolean rejectAppointment(Appointment appointment) {
        /*
         * Modifies the status variable in the passed appointment object to rejected & adds it to the appointment set
         */
        appointment.setStatus("rejected");
        return appointments.add(appointment);
    }
    public String viewReviewList() {
        StringBuilder sb = new StringBuilder();
        for (Review review : this.reviews)
            sb.append(review.getUuid().toString()).append("\n");
        return sb.toString();
    }

    public Barber(String firstName, String lastName, String email) {
        super(firstName, lastName, email, UserType.BARBER);
        this.reviews = new HashSet<Review>();
        this.appointments = new HashSet<Appointment>();
    }

}
