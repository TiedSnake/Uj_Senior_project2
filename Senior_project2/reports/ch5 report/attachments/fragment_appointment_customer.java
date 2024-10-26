package com.example.haircut;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class fragment_appointment_customer extends Fragment {

    private CalendarView calendarView;
    private TextView selectedDate;
    private LinearLayout timeSlotsContainer;
    private Button bookAppointmentButton;
    private TextView confirmationMessage;
    private FrameLayout overlayFrame; // Added FrameLayout for future content
    private String selectedTimeSlot; // Variable to hold the selected time slot

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_customer, container, false);

        calendarView = view.findViewById(R.id.calendar_view);
        selectedDate = view.findViewById(R.id.selected_date);
        timeSlotsContainer = view.findViewById(R.id.time_slots_container);
        bookAppointmentButton = view.findViewById(R.id.book_appointment_button);
        confirmationMessage = view.findViewById(R.id.confirmation_message);
        overlayFrame = view.findViewById(R.id.overlay_frame); // Initialize FrameLayout

        // Set up calendar listener
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            String date = dayOfMonth + "/" + (month + 1) + "/" + year;
            selectedDate.setText(date);
            showAvailableTimes(); // Call method to display available times
        });

        bookAppointmentButton.setOnClickListener(v -> bookAppointment());

        return view;
    }

    private void showAvailableTimes() {
        // Logic to show available time slots based on selected date
        timeSlotsContainer.removeAllViews(); // Clear previous time slots
        String[] timeSlots = {"10:00 AM", "11:00 AM", "1:00 PM"}; // Dummy time slots

        for (String timeSlot : timeSlots) {
            Button timeSlotButton = new Button(getContext());
            timeSlotButton.setText(timeSlot);
            timeSlotButton.setPadding(0, 10, 0, 10);
            timeSlotButton.setOnClickListener(v -> selectTimeSlot(timeSlot)); // Set click listener

            timeSlotsContainer.addView(timeSlotButton);
        }

        timeSlotsContainer.setVisibility(View.VISIBLE); // Show time slots
    }

    private void selectTimeSlot(String timeSlot) {
        selectedTimeSlot = timeSlot; // Save the selected time slot
        confirmationMessage.setText(""); // Clear previous confirmation message
    }

    private void bookAppointment() {
        if (selectedTimeSlot != null) {
            confirmationMessage.setText("Appointment booked for " + selectedDate.getText() + " at " + selectedTimeSlot);
            confirmationMessage.setVisibility(View.VISIBLE);
        } else {
            confirmationMessage.setText("Please select a time slot.");
            confirmationMessage.setVisibility(View.VISIBLE);
        }
    }
}

