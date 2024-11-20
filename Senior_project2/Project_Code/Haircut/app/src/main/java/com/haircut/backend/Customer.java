package com.haircut.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Customer extends User {
    private final List<com.haircut.backend.Appointment> appointments;
    public void reserveAppointment(UUID barberId) {
        /*
         * creates an appointment object with (barberId, date, customerId) & adds to appointments list
         */
    }

    public Barber viewBarberProfile(UUID barberId) {
        /*
        returns barber profile by passing the ID.
         */
        return null;
    }

    /**
     * Initiate a chatting forum with the barber
     *
     * @param barberId
     */
    public void beginChat(UUID barberId) {

    }

    /**
     * Rates a barber.
     *
     * @param barberId
     */
    public void rateBarber(UUID barberId) {
        /*
         * Creates a review object containing (customerId, date, barberId)
         *
         */
    }

    public void initiatePayment() {
        /*
         * needs payment method.
         *
         */
    }

    public Customer(String firstName, String lastName, String email) {
        super(firstName, lastName, email,  UserType.CUSTOMER);
        this.appointments = new ArrayList<Appointment>();
    }

}
